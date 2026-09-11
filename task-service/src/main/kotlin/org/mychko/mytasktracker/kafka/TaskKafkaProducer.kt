package org.mychko.mytasktracker.kafka

import org.mychko.mytasktracker.config.KafkaTopics.TASK_BATCH_EVENTS
import org.mychko.mytasktracker.config.KafkaTopics.TASK_EVENTS
import org.mychko.mytasktracker.dto.TaskBatchEvent
import org.mychko.mytasktracker.dto.TaskEvent

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class TaskKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
)
{
    companion object {
        private val log = LoggerFactory.getLogger(TaskKafkaProducer::class.java)
    }

    fun sendEvent(event: TaskEvent) {
        log.info("Отправляем событие в топик $TASK_EVENTS: {}", event)

        // В качестве ключа используем ID пользователя,
        // чтобы события по одному юзеру всегда попадали в одну партицию
        kafkaTemplate.send(TASK_EVENTS, event.taskId.toString(), event)
    }

    fun sendEvent(event: TaskBatchEvent) {
        log.info("Отправляем событие в топик $TASK_BATCH_EVENTS: {}", event)

        kafkaTemplate.send(TASK_BATCH_EVENTS, event)
    }
}