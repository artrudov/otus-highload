package com.otus.highload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
class HighloadApplication

fun main(args: Array<String>) {
    runApplication<HighloadApplication>(*args)
}
