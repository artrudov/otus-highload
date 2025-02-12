package com.otus.highload.configurations

import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
  @Bean
  fun jackson2JsonMessageConverter(): Jackson2JsonMessageConverter {
    return Jackson2JsonMessageConverter()
  }

  @Bean
  fun rabbitTemplate(
    connectionFactory: ConnectionFactory,
    jackson2JsonMessageConverter: Jackson2JsonMessageConverter
  ): RabbitTemplate {
    val rabbitTemplate = RabbitTemplate(connectionFactory)
    rabbitTemplate.messageConverter = jackson2JsonMessageConverter

    return rabbitTemplate
  }

  @Bean
  fun exchange(): Exchange {
    return TopicExchange(EXCHANGE_POSTS, false, false)
  }

  companion object {
    const val EXCHANGE_POSTS: String = "exchange.posts"

    fun toPostAuthorRoutingKey(authorId: Long): String {
      return "post.author.$authorId"
    }
  }
}