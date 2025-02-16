package com.otus.highload.configurations

import com.otus.highload.filters.RequestIdFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FilterConfiguration {
  @Bean
  fun requestIdFilterRegistration(filter: RequestIdFilter): FilterRegistrationBean<RequestIdFilter> {
    val registrationBean = FilterRegistrationBean<RequestIdFilter>()
    registrationBean.filter = filter
    registrationBean.addUrlPatterns("/*")
    registrationBean.order = 1

    return registrationBean
  }
}