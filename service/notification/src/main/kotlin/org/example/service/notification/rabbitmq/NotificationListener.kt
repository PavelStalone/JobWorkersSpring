package org.example.service.notification.rabbitmq

import org.example.event.RabbitMQ.FANOUT_EXCHANGE_NAME
import org.example.event.WorkerEvent
import org.example.service.notification.websocket.NotificationHandler
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class NotificationListener(
    private val notificationHandler: NotificationHandler,
) {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "q.notifications.browser", durable = "true"),
                exchange = Exchange(name = FANOUT_EXCHANGE_NAME, type = ExchangeTypes.FANOUT),
            )
        ]
    )
    fun handleUserRatedEvent(event: WorkerEvent.WorkerRatedEvent) {
        LOG.info("Received event from RabbitMQ: $event")

        val message = String.format(
            "{\"type\": \"RATING_UPDATE\", \"workerId\": %d, \"score\": %d, \"verdict\": \"%s\"}",
            event.workerId, event.score, event.verdict
        )

        notificationHandler.broadcast(message)
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(NotificationListener::class.java)
    }
}
