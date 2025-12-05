package org.example.service.notification.rabbitmq

import org.springframework.amqp.support.converter.SimpleMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun messageConverter() = SimpleMessageConverter().apply {
        setAllowedListPatterns(listOf("org.example.event.*"))
    }
}
