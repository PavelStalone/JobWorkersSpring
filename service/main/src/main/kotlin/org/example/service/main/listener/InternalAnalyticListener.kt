package org.example.service.main.listener

import org.example.event.RabbitMQ.FANOUT_EXCHANGE_NAME
import org.example.event.WorkerEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class InternalAnalyticListener {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(name = "q.main.analytic.log", durable = "true"),
                exchange = Exchange(name = FANOUT_EXCHANGE_NAME, type = ExchangeTypes.FANOUT),
            )
        ]
    )
    fun handleRating(event: WorkerEvent.WorkerRatedEvent){
        LOG.info("We just rated user: $event")
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(InternalAnalyticListener::class.java)
    }
}
