package com.rkbapps.note_taking

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(e:MethodArgumentNotValidException):ResponseEntity<Map<String,Any>>{
        val error = e.bindingResult.allErrors.map {
            it.defaultMessage?:"Invalid value"
        }
        return ResponseEntity.status(400).body(mapOf(
            "errors" to error
        ))
    }
}