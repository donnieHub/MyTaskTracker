package org.mychko.mytasktracker.service

import org.mychko.mytasktracker.dto.UserEvent
import org.mychko.mytasktracker.dto.UserEventType
import org.mychko.mytasktracker.exception.UserNotFoundException
import org.mychko.mytasktracker.kafka.UserKafkaProducer
import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repo: UserRepository,
    private val kafkaProducer: UserKafkaProducer
) {

    @Transactional
    fun create(user: User): User {
        val savedUser = repo.save(user)

        val savedUserId = savedUser.id ?: throw IllegalStateException("User ID must not be null after saving")
        val event = UserEvent(savedUserId, UserEventType.CREATED, savedUser)
        kafkaProducer.sendEvent(event)

        return savedUser
    }

    @Transactional(readOnly = true)
    fun getAll(): List<User> = repo.findAll()

    @Transactional(readOnly = true)
    fun getById(id: Long): User {
        val user = repo.findById(id).orElseThrow { UserNotFoundException(id) }

        val event = UserEvent(user.id!!, UserEventType.READ, user)
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

        val event = UserEvent(savedUser.id!!, UserEventType.UPDATED, savedUser)
        kafkaProducer.sendEvent(event)

        return savedUser
    }

    @Transactional
    fun delete(id: Long) {
        if (!repo.existsById(id)) {
            throw RuntimeException("User not found")
        }

        repo.deleteById(id)

        val event = UserEvent(id, UserEventType.DELETED, null)
        kafkaProducer.sendEvent(event)
    }
}