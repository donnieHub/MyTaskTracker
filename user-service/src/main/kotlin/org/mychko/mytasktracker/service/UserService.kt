package org.mychko.mytasktracker.service

import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repo: UserRepository) {

    fun create(user: User): User = repo.save(user)

    fun getAll(): List<User> = repo.findAll()

    fun getById(id: Long): User =
        repo.findById(id).orElseThrow { RuntimeException("User not found") }

    fun update(id: Long, updated: User): User {
        val user = getById(id)
        val newUser = user.apply {
            username = updated.username
            email = updated.email
            isActive = updated.isActive
        }
        return repo.save(newUser)
    }

    fun delete(id: Long) {
        if (!repo.existsById(id)) {
            throw RuntimeException("User not found")
        }
        repo.deleteById(id)
    }
}