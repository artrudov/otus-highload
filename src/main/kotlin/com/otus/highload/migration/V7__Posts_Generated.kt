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
class V7__Posts_Generated : BaseJavaMigration() {
  override fun migrate(context: Context) {
    val jdbcTemplate = JdbcTemplate(
      SingleConnectionDataSource(context.connection, true)
    )

    val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
    val batchedParams: MutableList<SqlParameterSource> = mutableListOf()

    println("${LocalDateTime.now()}: Filling batched params for creting posts")

    val commonPost = File("src/main/resources/db/migration/posts.txt").readText()

    for (i in 1..100_000) {
      val endIndex = Random.nextInt(500, 5000)
      val randomSubstring = commonPost.substring(0, endIndex)

      val params = mapOf(
        "userId" to Random.nextInt(1, 10000),
        "content" to randomSubstring,
      )

      batchedParams.add(MapSqlParameterSource(params))
    }

    println("${LocalDateTime.now()}: Filled batched params for posts")

    namedTemplate.batchUpdate(
      """
          INSERT INTO posts (user_id, content)
          VALUES (:userId, :content)
      """.trim(),
      batchedParams.toTypedArray()
    )

    println("${LocalDateTime.now()}: Inserted batched params for posts")
  }
}