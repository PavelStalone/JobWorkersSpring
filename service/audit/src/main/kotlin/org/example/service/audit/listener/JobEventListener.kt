package org.example.service.audit.listener

import org.example.event.JobEvent.JobCreatedEvent
import org.example.event.JobEvent.JobDeletedEvent
import org.example.event.RabbitMQ.EXCHANGE_NAME
import org.example.event.RabbitMQ.Notification
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class JobEventListener {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "notification-job-created", durable = "true"),
                exchange = Exchange(name = EXCHANGE_NAME, type = "topic"),
                key = [Notification.Job.Created.name],
            )
        ]
    )
    fun handleJobCreatedEvent(event: JobCreatedEvent) {
        LOG.info("Received new job event: $event")
    }

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "notification-job-deleted", durable = "true"),
                exchange = Exchange(name = EXCHANGE_NAME, type = "topic"),
                key = [Notification.Job.Deleted.name],
            )
        ]
    )
    fun handleJobDeletedEvent(event: JobDeletedEvent) {
        LOG.info("Job with id: ${event.jobId} was deleted")
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(JobEventListener::class.java)
    }
}
