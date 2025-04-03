package com.rkbapps.note_taking.controller.model

import jakarta.validation.constraints.NotBlank

data class NoteRequest (
    val id:String?,
    @NotBlank(message = "Title cannot be blank.")
    val title:String,
    val content:String,
    val color:Long,
)