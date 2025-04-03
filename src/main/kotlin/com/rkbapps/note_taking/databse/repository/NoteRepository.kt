package com.rkbapps.note_taking.databse.repository

import com.rkbapps.note_taking.databse.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository : MongoRepository<Note,ObjectId> {

    fun findByOwnerId(ownerId:ObjectId):List<Note>

}