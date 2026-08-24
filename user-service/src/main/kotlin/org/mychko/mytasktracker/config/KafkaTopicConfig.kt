package org.mychko.mytasktracker.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {

    @Bean
    fun userEventsTopic(): NewTopic {
        // Имя топика, 1 партиция, 1 фактор репликации (для тестов в 1 узел)
        return NewTopic("user-events", 3, 1.toShort())
    }
}