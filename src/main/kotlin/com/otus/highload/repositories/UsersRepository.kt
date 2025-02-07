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


val SQL_FIND_BY_USERNAME = "SELECT * FROM users WHERE username = :username"

@Repository
class UsersRepository {
  @Autowired
  lateinit var jdbcClient: JdbcClient

  fun createUser(newUser: NewUser): RegisteredUser? {
    val sql = """
            INSERT INTO users (username, password, first_name, second_name, city, biography, birthdate)
            VALUES (:username, :password, :firstName, :secondName, :city, :biography, :birthdate)
        """.trimIndent()

    val keyHolder = GeneratedKeyHolder()

    jdbcClient.sql(sql).params(
      mapOf(
        "username" to newUser.username,
        "password" to newUser.password,
        "firstName" to newUser.firstName,
        "secondName" to newUser.secondName,
        "city" to newUser.city,
        "biography" to newUser.biography,
        "birthdate" to newUser.birthdate,
      )
    ).update(keyHolder)

    return RegisteredUser(
      keyHolder.keys?.get("id") as Long,
      keyHolder.keys?.get("username").toString(),
      keyHolder.keys?.get("firstName").toString(),
      keyHolder.keys?.get("secondName").toString(),
      LocalDate.parse(keyHolder.keys?.get("birthdate").toString()),
      keyHolder.keys?.get("biography").toString(),
      keyHolder.keys?.get("city").toString(),
    )
  }

  fun findByUsername(username: String): User? {
    return jdbcClient
      .sql(SQL_FIND_BY_USERNAME)
      .params(mapOf("username" to username))
      .query(User::class.java)
      .single()
  }

  fun findById(userId: Long): RegisteredUser? {
    return jdbcClient
      .sql("SELECT * FROM users WHERE id = :userId")
      .params(mapOf("userId" to userId))
      .query(RegisteredUser::class.java)
      .single()
  }

  fun searchByFirstNameAndSecondName(firstName: String, secondName: String): List<RegisteredUser>? {
    return jdbcClient
      .sql(
        """
          SELECT * FROM users 
          WHERE second_name LIKE :secondName AND first_name LIKE :firstName
          ORDER BY id ASC 
        """.trimIndent()
      )
      .params(
        mapOf(
          "firstName" to firstName,
          "secondName" to secondName
        )
      )
      .query(RegisteredUser::class.java)
      .list()
  }
}