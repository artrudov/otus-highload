package com.otus.highload.interceptors

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException


class CustomHeaderInterceptor : ClientHttpRequestInterceptor {
  @Throws(IOException::class)
  override fun intercept(
    request: HttpRequest,
    body: ByteArray,
    execution: ClientHttpRequestExecution
  ): ClientHttpResponse {
    val name = customHeaderValue.get().first
    val value = customHeaderValue.get().second

    if (value.isNotEmpty() && name.isNotEmpty()) {
      request.headers.add(name, value)
    }
    return execution.execute(request, body)
  }

  companion object {
    private val customHeaderValue = ThreadLocal<Pair<String, String>>()

    fun setCustomHeaderValue(value: Pair<String, String>) {
      customHeaderValue.set(value)
    }

    fun clearCustomHeaderValue() {
      customHeaderValue.remove()
    }

  }
}