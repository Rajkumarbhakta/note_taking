package com.rkbapps.note_taking.controller.model

import java.time.Instant

data class NoteResponse(
    val id:String?,
    val title:String,
    val content:String,
    val color:Long,
    val createdAt:Instant
)
