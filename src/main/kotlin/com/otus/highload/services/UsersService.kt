package com.otus.highload.services

import com.otus.highload.domain.NewUser
import com.otus.highload.domain.RegisteredUser
import com.otus.highload.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class UsersService {
    @Autowired
    lateinit var usersRepository: UsersRepository

    fun createUser(newUser: NewUser): RegisteredUser? {
        newUser.password = BCryptPasswordEncoder().encode(newUser.password)

        return usersRepository.createUser(newUser)
    }

    fun getUserById(userId: BigInteger): RegisteredUser? {
        return usersRepository.findById(userId)
    }

    fun searchByFirstNameAndSecondName(firstName: String, secondName: String): List<RegisteredUser>? {
        return usersRepository.searchByFirstNameAndSecondName(firstName, secondName)
    }
}