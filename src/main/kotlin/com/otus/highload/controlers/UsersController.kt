package com.otus.highload.controlers

import com.otus.highload.domain.NewUser
import com.otus.highload.domain.RegisteredUser
import com.otus.highload.services.UsersService
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigInteger

@Controller
@RequestMapping("/users")
class UsersController {
    @Autowired
    private lateinit var usersService: UsersService

    @PostMapping
    fun createUser(@RequestBody newUser: NewUser): ResponseEntity<RegisteredUser> {
        val registeredUser = usersService.createUser(newUser)
        return ResponseEntity(registeredUser, HttpStatus.CREATED)
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: BigInteger): ResponseEntity<RegisteredUser> {
        val registeredUser = usersService.getUserById(userId)
        return ResponseEntity.ok(registeredUser)
    }

    @GetMapping("/search")
    @Validated
    fun searchUsers(@RequestParam @NotBlank firstName: String, @RequestParam @NotBlank lastName: String): ResponseEntity<List<RegisteredUser>> {
        val registeredUsers = usersService.searchByFirstNameAndSecondName(firstName, lastName)
        return ResponseEntity.ok(registeredUsers)
    }
}