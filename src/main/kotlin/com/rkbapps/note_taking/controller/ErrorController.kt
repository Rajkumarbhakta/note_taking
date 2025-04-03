package com.rkbapps.note_taking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController()
@RequestMapping("/error")
class ErrorController {

    @GetMapping
    fun errorMassage():Map<String,Any>{
        return mapOf(
            "message" to "this is a custom error message"
        )
    }

}