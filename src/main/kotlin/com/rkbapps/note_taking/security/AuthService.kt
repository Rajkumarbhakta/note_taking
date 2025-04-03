package com.rkbapps.note_taking.security

import com.rkbapps.note_taking.databse.model.RefreshToken
import com.rkbapps.note_taking.databse.model.User
import com.rkbapps.note_taking.databse.repository.RefreshTokenRepository
import com.rkbapps.note_taking.databse.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashedEncoder: HashedEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    data class TokenPair(
        val accessToken:String,
        val refreshToken:String,
    )

    fun register(email:String,password:String):User{
        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashedEncoder.encode(password)
            )
        )
    }

    fun login(email:String,password:String):TokenPair{
        val user = userRepository.findByEmail(email)?:throw BadCredentialsException("Invalid Credential")
        if(!hashedEncoder.matches(password,user.hashedPassword)){
            throw BadCredentialsException("Invalid Credential")
        }
        val newAccessToken = jwtService.generateAccessToken(userId = user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(userId = user.id.toHexString())

        storeRefreshToken(userId = user.id, rawToken = newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashed)
            ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(401),
                "Refresh token not recognized (maybe used or expired?)"
            )

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashed)

        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId:ObjectId,rawToken:String){
        val hashed = hashToken(rawToken)
        val expireMs = jwtService.refreshTokenValidityMs
        val expireAt = Instant.now().plusMillis(expireMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expireAt = expireAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token:String):String{
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

}