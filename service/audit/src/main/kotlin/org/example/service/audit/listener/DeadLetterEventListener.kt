package org.example.service.audit.listener

import org.example.event.Event
import org.example.event.RabbitMQ.DL_EXCHANGE_NAME
import org.example.event.RabbitMQ.Notification
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.converter.SimpleMessageConverter
import org.springframework.stereotype.Component

@Component
class DeadLetterEventListener(val messageConverter: SimpleMessageConverter) {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "notification-queue.dlq", durable = "true"),
                exchange = Exchange(name = DL_EXCHANGE_NAME, type = "topic"),
                key = [Notification.dead_name],
            )
        ]
    )
    fun handleDlqMessage(failedMessage: Any) {
        val convertedValue = (failedMessage as? Message)?.let(messageConverter::fromMessage)

        LOG.info("Received dead message: ${convertedValue ?: ""} $failedMessage")
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(DeadLetterEventListener::class.java)
    }
}
