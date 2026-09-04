package org.mychko.mytasktracker.kafka

import org.mychko.mytasktracker.dto.UserBatchEvent
import org.mychko.mytasktracker.dto.UserEvent
import org.mychko.mytasktracker.config.KafkaTopics.USER_EVENTS
import org.mychko.mytasktracker.config.KafkaTopics.USER_BATCH_EVENTS

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UserKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
)
{
    companion object {
        private val log = LoggerFactory.getLogger(UserKafkaProducer::class.java)
    }

    fun sendEvent(event: UserEvent) {
        log.info("Отправляем событие в топик $USER_EVENTS: {}", event)

        // В качестве ключа используем ID пользователя,
        // чтобы события по одному юзеру всегда попадали в одну партицию
        kafkaTemplate.send(USER_EVENTS, event.userId.toString(), event)
    }

    fun sendEvent(event: UserBatchEvent) {
        log.info("Отправляем событие в топик $USER_BATCH_EVENTS: {}", event)

        kafkaTemplate.send(USER_BATCH_EVENTS, event)
    }
}