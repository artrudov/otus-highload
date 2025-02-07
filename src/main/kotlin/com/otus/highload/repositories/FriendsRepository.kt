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

  fun add(friendId: Long, username: Long) {
    val sql = "INSERT INTO friends (user_id, friend_id) VALUES (:userId, :friendId)"

    println(jdbcClient.sql(sql).params(mapOf(Pair("userId", username), Pair("friendId", friendId))).update())
  }

  fun delete(friendId: Long, username: Long) {
    val sql = "DELETE FROM friends WHERE user_id = :userId AND friend_id = :friendId"

    println(jdbcClient.sql(sql).params(mapOf(Pair("userId", username), Pair("friendId", friendId))).update())
  }
}