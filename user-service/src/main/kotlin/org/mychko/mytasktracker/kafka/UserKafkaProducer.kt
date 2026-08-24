package org.mychko.mytasktracker.kafka

import org.mychko.mytasktracker.dto.UserEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UserKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, UserEvent>
)
{
    private val log = LoggerFactory.getLogger(javaClass)
    private val topicName = "user-events"

    fun sendEvent(event: UserEvent) {
        log.info("Отправляем событие в Kafka: {}", event)

        // В качестве ключа используем ID пользователя,
        // чтобы события по одному юзеру всегда попадали в одну партицию
        kafkaTemplate.send(topicName, event.userId.toString(), event)
    }
}