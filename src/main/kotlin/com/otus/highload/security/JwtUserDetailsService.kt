package com.otus.highload.security

import com.otus.highload.repositories.UsersRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtUserDetailsService(
    private val usersRepository: UsersRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User $username not found!")

        return User.builder()
            .username(user.username)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}