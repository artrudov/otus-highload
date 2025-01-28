package com.otus.highload.controlers

import com.otus.highload.domain.AuthenticationRequest
import com.otus.highload.domain.AuthenticationResponse
import com.otus.highload.domain.RefreshTokenRequest
import com.otus.highload.domain.TokenResponse
import com.otus.highload.services.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/login")
    fun authenticate(
        @RequestBody authRequest: AuthenticationRequest
    ): AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): TokenResponse = TokenResponse(token = authenticationService.refreshAccessToken(request.token))
}