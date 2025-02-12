package com.otus.highload.handlers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.otus.highload.configurations.RabbitMQConfig
import com.otus.highload.repositories.FriendsRepository
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


data class UserId(
  val userId: Long
)

@Component
class FeedWebSocketHandler : TextWebSocketHandler() {
  val log: Logger = LoggerFactory.getLogger(FeedWebSocketHandler::class.java)

  @Autowired
  lateinit var rabbitTemplate: RabbitTemplate

  @Autowired
  lateinit var friendsRepository: FriendsRepository

  override fun afterConnectionEstablished(session: WebSocketSession) {
    println(session.attributes)
  }

  override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
    log.info("WebSocket session closed: {}, status='{}'", session, status)

    val userId = session.attributes.getOrDefault("userId", null)
    val queueName = "user.queue.$userId"

    rabbitTemplate.execute { ch: Channel ->
      ch.queueDelete(
        "user.queue.$userId"
      )
    }

    log.info("Deleted queue '{}'", queueName)
  }

  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    log.info("Get user id from websocket message")

    val mapper = jacksonObjectMapper()
    val parsedMessage: UserId = mapper.readValue(message.payload, UserId::class.java)

    session.attributes["userId"] = parsedMessage.userId
    createQueueAndConsumer(parsedMessage.userId, session);
  }

  private fun createQueueAndConsumer(userId: Long, session: WebSocketSession) {
    val name = "user.queue.$userId"

    rabbitTemplate.execute<Any> { ch: Channel ->
      ch.queueDeclare(name, false, true, true, null)
      log.info("Created queue '{}'", name)

      val friends = friendsRepository.getAllFriendsId(userId)

      for (friendId in friends) {
        val routingKey = RabbitMQConfig.toPostAuthorRoutingKey(friendId)
        ch.queueBind(
          name,
          RabbitMQConfig.EXCHANGE_POSTS,
          routingKey
        )
        log.info("Created queue binding: user-id={}, routing-key={}", userId, routingKey)
      }
      log.info("Created {} queue bindings: user-id={}", friends.size, userId)

      ch.basicConsume(
        name,
        true,
        { _: String?, message: Delivery ->
          val wsMessage = String(message.body)
          session.sendMessage(TextMessage(wsMessage))
          log.info("Sent message to websocket: user-id={}, message='{}'", userId, wsMessage)
        },
        { _: String? -> }
      )
      null
    }
  }
}