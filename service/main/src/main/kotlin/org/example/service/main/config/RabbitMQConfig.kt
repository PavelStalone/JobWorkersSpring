package org.example.service.main.config

import org.example.event.RabbitMQ.EXCHANGE_NAME
import org.example.event.RabbitMQ.FANOUT_EXCHANGE_NAME
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.SimpleMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun repairExchange() = TopicExchange(EXCHANGE_NAME, true, false)

    @Bean
    fun fanoutExchange() = FanoutExchange(FANOUT_EXCHANGE_NAME, true, false)

    @Bean
    fun messageConverter() = SimpleMessageConverter().apply {
        setAllowedListPatterns(listOf("org.example.event.*"))
    }

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        simpleMessageConverter: SimpleMessageConverter
    ) = RabbitTemplate(connectionFactory).apply {
        messageConverter = simpleMessageConverter
        setConfirmCallback { correlationData, ack, cause ->
            if (!ack) {
                println("NACK: Message delivery failed! $cause")
            }
        }
    }
}
