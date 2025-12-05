package org.example.service.notification.controller

import org.example.service.notification.handler.NotificationWebSocketHandler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val handler: NotificationWebSocketHandler,
) {

    @PostMapping("/broadcast")
    fun broadcast(@RequestBody message: String): ResponseEntity<Map<String, Any>> {
        val sent = handler.broadcast(message)

        return ResponseEntity.ok(mapOf(
            "status" to "ok",
            "sentTo" to sent,
            "message" to message
        ))
    }

    @PostMapping("/personal")
    fun personal(@RequestBody personalMessage: PersonalMessage): ResponseEntity<Map<String, Any>> {
        val sent = handler.sentByUserId(userId = personalMessage.userId, message = personalMessage.message)

        return ResponseEntity.ok(mapOf(
            "status" to "ok",
            "message" to personalMessage.message
        ))
    }

    @GetMapping("/stats")
    fun stats(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(mapOf(
            "activeConnections" to handler.activeConnections
        ))
    }
}

data class PersonalMessage(
    val userId: String,
    val message: String,
)
