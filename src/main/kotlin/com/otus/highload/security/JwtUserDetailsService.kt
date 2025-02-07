package com.otus.highload.security

import com.otus.highload.repositories.UsersRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomUser : User {
    var id: Long

    constructor(
        username: String?,
        password: String?,
        authorities: Collection<GrantedAuthority?>,
        id: Long
    ) : super(username, password, authorities) {
        this.id = id
    }
}

class JwtUserDetailsService(
    private val usersRepository: UsersRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User $username not found!")

        val roles: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(user.role.name))

        return CustomUser(user.username, user.password, roles, user.id)
    }
}