package org.example.service.notification.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val notificationHandler: NotificationHandler,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(notificationHandler, "/ws/notifications")
            .setAllowedOrigins("*")
    }
}