package com.rkbapps.note_taking.databse.repository

import com.rkbapps.note_taking.databse.model.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository:MongoRepository<RefreshToken,ObjectId> {

    fun findByUserIdAndHashedToken(userId:ObjectId,hashedToken:String):RefreshToken?
    fun deleteByUserIdAndHashedToken(userId:ObjectId,hashedToken: String)

}