package com.otus.highload.Users

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("users")
class UsersController {
    @GetMapping()
    fun getUsers(): List<String> {
        return listOf("")
    }
}