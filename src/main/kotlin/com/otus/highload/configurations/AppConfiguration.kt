package com.otus.highload.configurations

import com.otus.highload.interceptors.CustomHeaderInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class AppConfiguration {
  @Bean
  fun restTemplate(): RestTemplate {
    val restTemplate = RestTemplate()

    restTemplate.interceptors = listOf(CustomHeaderInterceptor())

    return restTemplate
  }
}