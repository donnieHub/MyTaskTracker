package org.mychko.mytasktracker.config

import org.apache.kafka.clients.admin.NewTopic
import org.mychko.mytasktracker.config.KafkaTopics.USER_BATCH_EVENTS
import org.mychko.mytasktracker.config.KafkaTopics.USER_EVENTS
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {

    @Bean
    fun userEventsTopic(): NewTopic {
        // Имя топика, 1 партиция, 1 фактор репликации (для тестов в 1 узел)
        return NewTopic(USER_EVENTS, 3, 1.toShort())
    }

    @Bean
    fun userBatchEventsTopic(): NewTopic {
        // Топик для батч-событий чтения (getAll), 1 партиция достаточно —
        // порядок между сообщениями не важен, объём сообщений сильно меньше
        return NewTopic(USER_BATCH_EVENTS, 1, 1.toShort())
    }
}