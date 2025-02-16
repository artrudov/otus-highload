package com.otus.highload.filters

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*


@Component
class RequestIdFilter : Filter {
  @Throws(IOException::class, ServletException::class)
  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val httpRequest = request as HttpServletRequest
    val httpResponse = response as HttpServletResponse

    val requestId: String = UUID.randomUUID().toString()

    httpRequest.setAttribute("X-Request-ID", requestId)
    httpResponse.setHeader("X-Request-ID", requestId)

    println(">>> Do filter for request id: ${httpRequest.getAttribute("X-Request-ID")}")

    chain.doFilter(request, response)
  }
}