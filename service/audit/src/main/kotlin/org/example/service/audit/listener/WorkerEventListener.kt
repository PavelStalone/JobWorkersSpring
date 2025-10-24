package org.example.service.audit.listener

import org.example.event.RabbitMQ.ALL_WILDCARD
import org.example.event.RabbitMQ.EXCHANGE_NAME
import org.example.event.RabbitMQ.Notification
import org.example.event.WorkerEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class WorkerEventListener {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "notification-worker-created", durable = "true"),
                exchange = Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                key = [Notification.Worker.Created.name],
            )
        ]
    )
    fun handleWorkerCreatedEvent(event: WorkerEvent.WorkerCreatedEvent) {
        LOG.info("Received new worker event: $event")
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
