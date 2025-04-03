package com.rkbapps.note_taking.databse.repository

import com.rkbapps.note_taking.databse.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository :MongoRepository<User,ObjectId> {

    fun findByEmail(email:String):User?
}