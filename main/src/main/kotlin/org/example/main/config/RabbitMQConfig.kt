package org.example.main.config

import org.example.event.RabbitMQ.EXCHANGE_NAME
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun repairExchange() = TopicExchange(EXCHANGE_NAME)
}
