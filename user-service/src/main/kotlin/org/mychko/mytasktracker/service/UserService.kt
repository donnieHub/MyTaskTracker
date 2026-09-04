package org.mychko.mytasktracker.service

import org.mychko.mytasktracker.dto.UserBatchEvent
import org.mychko.mytasktracker.dto.UserEvent
import org.mychko.mytasktracker.dto.UserEventType
import org.mychko.mytasktracker.dto.UserPatchRequest
import org.mychko.mytasktracker.exception.UserNotFoundException
import org.mychko.mytasktracker.kafka.UserKafkaProducer
import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repo: UserRepository,
    private val kafkaProducer: UserKafkaProducer
) {

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }

    @Transactional
    fun create(user: User): User {
        val savedUser = repo.save(user)

        val savedUserId = savedUser.id ?: throw IllegalStateException("User ID must not be null after saving")
        val event = UserEvent(savedUserId, UserEventType.CREATED, savedUser)
        kafkaProducer.sendEvent(event)

        return savedUser
    }

    @Transactional(readOnly = true)
    fun getAll(): List<User> {
        val savedUsers = repo.findAll()

        if (savedUsers.isNotEmpty()) {
            val event = UserBatchEvent(savedUsers.mapNotNull { it.id }, UserEventType.READ)
            runCatching { kafkaProducer.sendEvent(event) }
                .onFailure { log.warn("Не удалось отправить батч-событие READ для getAll", it) }
        }

        return savedUsers
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): User {
        val user = repo.findById(id).orElseThrow { UserNotFoundException(id) }

        val event = UserEvent(id, UserEventType.READ, user)
        kafkaProducer.sendEvent(event)

        return user
    }

    @Transactional
    fun update(id: Long, updated: User): User {
        val user = getById(id)
        val newUser = user.apply {
            username = updated.username
            email = updated.email
            isActive = updated.isActive
        }

        val savedUser = repo.save(newUser)

        val event = UserEvent(id, UserEventType.UPDATED, savedUser)
        kafkaProducer.sendEvent(event)

        return savedUser
    }

    @Transactional
    fun patch(id: Long, patch: UserPatchRequest): User {
        val user = getById(id)

        patch.username?.let { user.username = it }
        patch.email?.let { user.email = it }
        patch.isActive?.let { user.isActive = it }

        val savedUser = repo.save(user)

        runCatching {
            kafkaProducer.sendEvent(UserEvent(id, UserEventType.UPDATED, savedUser))
        }.onFailure { log.warn("Failed to publish UPDATED event for user $id", it) }

        return savedUser
    }

    @Transactional
    fun delete(id: Long) {
        val user = repo.findById(id).orElseThrow { UserNotFoundException(id) }

        repo.delete(user)

        val event = UserEvent(id, UserEventType.DELETED, null)
        kafkaProducer.sendEvent(event)
    }
}