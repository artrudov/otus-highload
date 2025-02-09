package com.otus.highload.domain

import java.io.Serializable

data class Post(
  val text: String
)

data class CreatedPost(
  val postId: Long,
  val userId: Long,
  val content: String
) : Serializable