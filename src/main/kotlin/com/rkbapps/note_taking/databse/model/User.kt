package com.rkbapps.note_taking.databse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
   @Id val id:ObjectId = ObjectId.get(),
    val email:String,
    val hashedPassword:String
)
