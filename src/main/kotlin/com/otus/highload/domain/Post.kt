package com.otus.highload.domain

data class Post(
  val text: String
)

data class CreatedPost (
  val postId: Long,
  val userId: Long,
  val content: String
)