package com.rkbapps.note_taking.controller

import com.rkbapps.note_taking.controller.model.NoteRequest
import com.rkbapps.note_taking.controller.model.NoteResponse
import com.rkbapps.note_taking.databse.model.Note
import com.rkbapps.note_taking.databse.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val repository: NoteRepository
) {

    @PostMapping
    fun saveNote(@RequestBody body:NoteRequest):NoteResponse{
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
      val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) }?:ObjectId.get(),
                title = body.title,
                color = body.color,
                content = body.content,
                ownerId = ObjectId(ownerId),
                createdAt = Instant.now()
            )
        )
      return  NoteResponse(
          id = note.id.toHexString(),
          title = note.title,
          color = note.color,
          content = note.content,
          createdAt = note.createdAt
      )
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            NoteResponse(
                id = it.id.toHexString(),
                title = it.title,
                color = it.color,
                content = it.content,
                createdAt = it.createdAt
            )
        }
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable("id") id:String){

        val note = repository.findById(ObjectId(id)).orElseThrow { IllegalArgumentException("Note not found.") }
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String

        if(note.ownerId.toHexString() == ownerId){
            repository.deleteById(ObjectId(id))
        }



    }

    

}