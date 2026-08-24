package org.mychko.mytasktracker.kafka

import org.mychko.mytasktracker.dto.UserEvent
import org.mychko.mytasktracker.dto.UserEventType.*
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserKafkaConsumer {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["user-events"], groupId = "user-group")
    fun consumeEvent(event: UserEvent) {
        log.info("Получено событие из Kafka: {}", event)

        when (event.eventType) {
            CREATED -> log.info("Новый пользователь создан: {}", event.user)
            UPDATED -> log.info("Пользователь обновлен: {}", event.user)
            DELETED -> log.info("Пользователь удален с ID: {}", event.userId)
            READ -> log.info("Пользователь получен: {}", event.user)
            else -> log.error("Неизвестный тип события: {}", event.eventType)
        }
    }
}