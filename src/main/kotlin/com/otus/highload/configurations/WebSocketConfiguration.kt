package com.otus.highload.configurations

import com.otus.highload.handlers.FeedWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor


@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry
      .addHandler(feedHandler(), "/posts/feed/posted")
      .addInterceptors(HttpSessionHandshakeInterceptor())
      .setAllowedOrigins("*")
  }

  @Bean
  fun feedHandler(): WebSocketHandler {
    return FeedWebSocketHandler()
  }
}