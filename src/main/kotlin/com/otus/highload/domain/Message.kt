package com.otus.highload.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class NewMessage(val text: String)

data class Message(
  var userFrom: Long,
  var userTo: Long,
  val author: Long,
  val text: String
)

data class MessageResponse(
  val author: Long,
  val text: String
)