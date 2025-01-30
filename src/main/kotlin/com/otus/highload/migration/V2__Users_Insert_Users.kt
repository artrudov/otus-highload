package com.otus.highload.migration

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDate

import java.time.LocalDateTime

@Component
class V2__Users_Insert_Users : BaseJavaMigration() {
  override fun migrate(context: Context) {
    val jdbcTemplate = JdbcTemplate(
      SingleConnectionDataSource(context.connection, true)
    )

    val namedTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
    val batchedParams: MutableList<SqlParameterSource> = mutableListOf()
    val password = BCryptPasswordEncoder().encode("123123Qw!")

    println("${LocalDateTime.now()}: Filling batched params from csv file")

    val bufferedReader = File("src/main/resources/db/migration/users.csv").bufferedReader()
    val csvRecord = CSVParser(bufferedReader, CSVFormat.DEFAULT)

    csvRecord.records.forEach  {
      val row = it.values()

      val name = row[0].split(" ")
      val firstName = name[1]
      val secondName = name[0]
      val birthdate = LocalDate.parse(row[1])
      val city = row[2]
      val username = "$firstName${row.hashCode()}$secondName"

      val params = mapOf(
        "username" to username,
        "password" to password,
        "firstName" to firstName,
        "secondName" to secondName,
        "city" to city,
        "birthdate" to birthdate
      )

      batchedParams.add(MapSqlParameterSource(params))
    }

    println("${LocalDateTime.now()}: Filled batched params from csv file")

    namedTemplate.batchUpdate(
      """
          INSERT INTO users (username, password, first_name, second_name, city, birthdate)
          VALUES (:username, :password, :firstName, :secondName, :city, :birthdate)
      """.trim(),
      batchedParams.toTypedArray()
    )

    println("${LocalDateTime.now()}: Inserted batched params")
  }
}