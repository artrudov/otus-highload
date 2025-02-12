package com.otus.highload.repositories

import com.otus.highload.domain.NewUser
import com.otus.highload.domain.RegisteredUser
import com.otus.highload.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.time.LocalDate


@Repository
class FriendsRepository {
  @Autowired
  lateinit var jdbcClient: JdbcClient

  fun getAllFriendsId(userId: Long): List<Long> {
    val sql = "SELECT friend_id FROM friends WHERE user_id = :userId"

    return jdbcClient
      .sql(sql)
      .params(mapOf("userId" to userId))
      .query(Long::class.java)
      .list()
  }

  fun add(friendId: Long, username: Long) {
    val sql = "INSERT INTO friends (user_id, friend_id) VALUES (:userId, :friendId)"

    jdbcClient
      .sql(sql)
      .params(mapOf(
        "userId" to username,
        "friendId" to friendId
      ))
      .query()
  }

  fun delete(friendId: Long, username: Long) {
    val sql = "DELETE FROM friends WHERE user_id = :userId AND friend_id = :friendId"

    jdbcClient
      .sql(sql)
      .params(mapOf("userId" to username, "friendId" to friendId))
      .query()
  }
}