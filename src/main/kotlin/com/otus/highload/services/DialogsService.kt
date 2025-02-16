package com.otus.highload.services

import com.otus.highload.domain.Message
import com.otus.highload.interceptors.CustomHeaderInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

const val dialogsApiUrl = "http://localhost:8081/api/v2/dialogs"

@Service
class DialogsService {
  @Autowired
  lateinit var restTemplate: RestTemplate

  fun save(message: Message, requestId: String): String? {
    val request = HttpEntity(message)

    CustomHeaderInterceptor.setCustomHeaderValue(Pair("X-Request-ID", requestId));
    val result = restTemplate.postForObject(dialogsApiUrl, request, String::class.java)
    CustomHeaderInterceptor.clearCustomHeaderValue();

    return result
  }

  fun findAllMessages(
    recipientID: Long,
    sender: Long,
    pageable: Pageable,
    requestId: String
  ): List<Message> {
    val uri = UriComponentsBuilder.fromUriString("$dialogsApiUrl/$recipientID")
      .queryParam("userFrom", sender)
      .queryParam("page", pageable.pageNumber)
      .queryParam("size", pageable.pageSize)
      .toUriString()

    CustomHeaderInterceptor.setCustomHeaderValue(Pair("X-Request-ID", requestId));
    val response = restTemplate.getForObject(uri, Array<Message>::class.java)
    CustomHeaderInterceptor.clearCustomHeaderValue();

    return response?.toList() ?: emptyList()
  }
}