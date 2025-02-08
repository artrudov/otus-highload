package com.otus.highload.repositories

import com.otus.highload.domain.CreatedPost
import com.otus.highload.domain.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class PostsRepository {
  @Autowired
  lateinit var jdbcClient: JdbcClient

  fun get(postId: Long): CreatedPost {
    val sql = "SELECT * FROM posts WHERE post_id = :postId"

    return jdbcClient
      .sql(sql)
      .params(mapOf("postId" to postId))
      .query(CreatedPost::class.java)
      .single()
  }

  fun save(post: Post, userId: Long): CreatedPost {
    val sql = "INSERT INTO posts (user_id, content) VALUES (:userId, :content)"

    val keyHolder = GeneratedKeyHolder()

    jdbcClient
      .sql(sql)
      .params(mapOf(
        "userId" to userId,
        "content" to post.text
      ))
      .update(keyHolder)

    return CreatedPost(
      postId = keyHolder.keys?.get("post_id").toString().toLong(),
      content = keyHolder.keys?.get("content").toString(),
      userId = keyHolder.keys?.get("user_id").toString().toLong(),
    )
  }

  fun update(post: CreatedPost): CreatedPost {
    val sql = "UPDATE posts SET content = :content WHERE post_id = :postId"

    jdbcClient
      .sql(sql)
      .params(mapOf(
        "postId" to post.postId,
        "content" to post.content
      ))
      .update()

    return post
  }

  fun delete(postId: Long, userId: Long) {
    val sql = "DELETE FROM posts WHERE post_id = :postId"

    jdbcClient.sql(sql).query()
  }
}