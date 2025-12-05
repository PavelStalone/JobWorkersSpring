package org.example.service.notification.websocket

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class NotificationHandler : TextWebSocketHandler() {

    private val sessions: MutableSet<WebSocketSession> = ConcurrentHashMap.newKeySet()

    val activeConnections: Int
        get() = sessions.size

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)

        LOG.info("Новое подключение: id=${session.id}, всего активных: ${sessions.size}")
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

        val countSent = sessions.fold(initial = 0) { acc, session ->
            acc + (1.takeIf { sendMessage(session, textMessage).isSuccess } ?: 0)
        }

        LOG.info("Broadcast: отправлено $countSent/${sessions.size} клиентам")

        return countSent
    }

    private fun sendMessage(session: WebSocketSession, message: TextMessage) = runCatching {
        check(session.isOpen) { "Session is closed" }

        synchronized(session) { session.sendMessage(message) }
    }.onFailure { exception ->
        LOG.warn("Ошибка отправки в сессию ${session.id}: ${exception.message}")

        removeSession(session)
    }

    private fun removeSession(session: WebSocketSession) {
        sessions.remove(session)
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(NotificationHandler::class.java)
    }
}
