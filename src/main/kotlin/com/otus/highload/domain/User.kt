package com.otus.highload.domain

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import java.math.BigInteger
import java.time.LocalDate

data class User(
    val id: BigInteger,
    val username: String,
    val password: String,
    val role: Role
)

enum class Role {
    USER, ADMIN
}

data class NewUser (
    @NotBlank
    @Size(min = 6, max = 50)
    val username: String,

    @NotBlank
    @Size(min = 8, max = 64)
    var password: String,

    @NotBlank
    @Size(min = 3, max = 50)
    val firstName: String,

    @NotBlank
    @Size(min = 3, max = 50)
    val secondName: String,

    @NotBlank
    @Past
    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    val birthdate: LocalDate,

    @Size(max = 1500)
    val biography: String,

    @NotBlank
    @Size(min = 3, max = 100)
    val city: String,
)

data class RegisteredUser (
    val id: Long,
    val username: String,
    val firstName: String,
    val secondName: String,
    val birthdate: LocalDate,
    val biography: String?,
    val city: String,
)