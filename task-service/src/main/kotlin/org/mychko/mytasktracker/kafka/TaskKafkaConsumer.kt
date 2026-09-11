package org.mychko.mytasktracker.kafka

import org.mychko.mytasktracker.config.KafkaTopics.TASK_EVENTS
import org.mychko.mytasktracker.dto.TaskEvent
import org.mychko.mytasktracker.dto.TaskEventType.*
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TaskKafkaConsumer {
    companion object {
        private val log = LoggerFactory.getLogger(TaskKafkaConsumer::class.java)
    }

    @KafkaListener(topics = [TASK_EVENTS], groupId = "task-group")
    fun consumeEvent(event: TaskEvent) {
        log.info("Получено событие из Kafka: {}", event)

        when (event.eventType) {
            CREATED -> log.info("Новый пользователь создан: {}", event.task)
            UPDATED -> log.info("Пользователь обновлен: {}", event.task)
            DELETED -> log.info("Пользователь удален с ID: {}", event.taskId)
            READ -> log.info("Пользователь получен: {}", event.task)
            else -> log.error("Неизвестный тип события: {}", event.eventType)
        }
    }
}