package com.otus.highload.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Component
import java.io.File

import java.time.LocalDateTime
import kotlin.random.Random

@Component
class V8__Users_Friends_Generated : BaseJavaMigration() {
  override fun migrate(context: Context) {
    val jdbcTemplate = JdbcTemplate(
      SingleConnectionDataSource(context.connection, true)
    )

    val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
    val batchedParams: MutableList<SqlParameterSource> = mutableListOf()

    println("${LocalDateTime.now()}: Filling batched params for creating friends")

    for (i in 1..500) {
      val params = mapOf(
        "userId" to Random.nextInt(1, 1_000),
        "friendId" to Random.nextInt(1, 10_000),
      )

      batchedParams.add(MapSqlParameterSource(params))
    }

    println("${LocalDateTime.now()}: Filled batched params for friends")

    namedTemplate.batchUpdate(
      """
          INSERT INTO friends (user_id, friend_id)
          VALUES (:userId, :friendId)
      """.trim(),
      batchedParams.toTypedArray()
    )

    println("${LocalDateTime.now()}: Inserted batched params for friends")
  }
}