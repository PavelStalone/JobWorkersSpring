package org.example.service.audit.listener

import com.rabbitmq.client.Channel
import org.example.event.RabbitMQ.ALL_WILDCARD
import org.example.event.RabbitMQ.DL_EXCHANGE_NAME
import org.example.event.RabbitMQ.EXCHANGE_NAME
import org.example.event.RabbitMQ.Notification
import org.example.event.WorkerEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.*
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class WorkerEventListener {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(
                    name = "notification-worker-created", durable = "true",
                    arguments = [
                        Argument(name = "x-dead-letter-exchange", value = DL_EXCHANGE_NAME),
                        Argument(name = "x-dead-letter-routing-key", value = Notification.dead_name),
                    ]
                ),
                exchange = Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = [Notification.Worker.Created.name],
            )
        ]
    )
    fun handleWorkerCreatedEvent(
        @Payload event: WorkerEvent.WorkerCreatedEvent,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long,
    ) {
        runCatching {
            LOG.info("Received new worker event: $event")

            require(event.name != "CRASH") { "Simulating processing error for DLQ test" }
        }.onSuccess {
            LOG.info("Notification sent for new worker $event")

            channel.basicAck(deliveryTag, false)
        }.onFailure { cause ->
            LOG.error("Failed to process event: $event", cause)

            channel.basicNack(deliveryTag, false, false)
        }
    }

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "notification-worker-deleted", durable = "true"),
                exchange = Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = [Notification.Worker.Deleted.name],
            )
        ]
    )
    fun handleWorkerDeletedEvent(event: WorkerEvent.WorkerDeletedEvent) {
        LOG.info("Worker with id: ${event.workerId} was deleted")
    }

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "notification-worker", durable = "true"),
                exchange = Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = [Notification.Worker.name + ".$ALL_WILDCARD"]
            )
        ]
    )
    fun handleWorkerEvent(event: WorkerEvent) {
        LOG.info("Found general worker event: $event")
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(WorkerEventListener::class.java)
    }
}
