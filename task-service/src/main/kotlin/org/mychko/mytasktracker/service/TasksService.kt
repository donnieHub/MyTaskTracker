package org.mychko.mytasktracker.service

import org.mychko.mytasktracker.dto.*
import org.mychko.mytasktracker.exception.TaskNotFoundException
import org.mychko.mytasktracker.kafka.TaskKafkaProducer
import org.mychko.mytasktracker.model.Task
import org.mychko.mytasktracker.repository.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TasksService(
    private val repo: TaskRepository,
    private val kafkaProducer: TaskKafkaProducer
) {

    companion object {
        private val log = LoggerFactory.getLogger(TasksService::class.java)
    }

    @Transactional
    fun create(request: CreateTaskRequest): Task {
        val userId = requireNotNull(request.userId) { "userId is required" }

        val task = Task(
            title = request.title,
            description = request.description ?: "",
            userId = userId
        )

        return repo.save(task)
    }

    @Transactional(readOnly = true)
    fun getTask(id: Long): Task =
        repo.findById(id)
            .orElseThrow { NoSuchElementException("Task not found: $id") }

    @Transactional(readOnly = true)
    fun getTasksByUserId(userId: Long): List<Task> =
        repo.findByUserId(userId)

    @Transactional
    fun patch(id: Long, request: TaskPatchRequest): Task {
        val task = getTask(id)
        request.title?.let { task.title = it }
        request.description?.let { task.description = it }
        request.status?.let { task.status = it }

        val savedTask = repo.save(task)

        runCatching {
            val event = TaskEvent(id, TaskEventType.UPDATED, savedTask)
            kafkaProducer.sendEvent(event)
        }.onFailure { log.warn("Failed to publish UPDATED event for task $id", it) }

        return savedTask
    }

    @Transactional
    fun delete(id: Long) {
        val task = repo.findById(id).orElseThrow { TaskNotFoundException(id) }

        repo.delete(task)

        val event = TaskEvent(id, TaskEventType.DELETED, null)
        kafkaProducer.sendEvent(event)
    }
}