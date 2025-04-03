package com.rkbapps.note_taking.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashedEncoder {

    private val byCrypt = BCryptPasswordEncoder()

    fun encode(raw:String):String = byCrypt.encode(raw)

    fun matches(raw:String,hashed:String):Boolean = byCrypt.matches(raw,hashed)

}