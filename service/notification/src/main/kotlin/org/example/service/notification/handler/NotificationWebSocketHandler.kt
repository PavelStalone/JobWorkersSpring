package org.example.service.notification.handler

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class NotificationWebSocketHandler : TextWebSocketHandler() {

    private val sessions: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    val activeConnections: Int
        get() = sessions.size

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = requireNotNull(session.uri?.query?.split("=")?.last())

        sessions[userId] = session

        LOG.info("Новое подключение для пользователя $userId: id=${session.id}, всего активных: ${sessions.size}")
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage
    ) {
        val payload = message.payload

        LOG.debug("Сообщение от ${session.id}: $payload")

        if ("PING".equals(payload, true)) {
            LOG.info("Отправлено PONG на ${session.id}")
            sendMessage(session, TextMessage("PONG"))
        }
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        removeSession(session)

        LOG.info("Отключение: id=${session.id}, причина=${status.reason}, осталось: ${sessions.size}")
    }

    override fun handleTransportError(
        session: WebSocketSession,
        exception: Throwable
    ) {
        LOG.error("Ошибка транспорта для сессии ${session.id}: ${exception.message}")

        removeSession(session)
    }

    fun broadcast(message: String): Int {
        val textMessage = TextMessage(message)

        val countSent = sessions.values.fold(initial = 0) { acc, session ->
            acc + (1.takeIf { sendMessage(session, textMessage).isSuccess } ?: 0)
        }

        LOG.info("Broadcast: отправлено $countSent/${sessions.size} клиентам")

        return countSent
    }

    fun sentByUserId(userId: String, message: String): Result<Unit> = runCatching {
        val textMessage = TextMessage(message)
        val session = requireNotNull(sessions[userId])

        sendMessage(session, textMessage)
    }

    private fun sendMessage(session: WebSocketSession, message: TextMessage) = runCatching {
        check(session.isOpen) { "Session is closed" }

        session.sendMessage(message)
    }.onFailure { exception ->
        LOG.warn("Ошибка отправки в сессию ${session.id}: ${exception.message}")

        removeSession(session)
    }

    private fun removeSession(session: WebSocketSession) {
        if (sessions.containsValue(session)) {
            sessions.remove(sessions.filterValues { it == session }.keys.first())
        }
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(NotificationWebSocketHandler::class.java)
    }
}
