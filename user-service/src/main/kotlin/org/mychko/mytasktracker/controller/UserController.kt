package org.mychko.mytasktracker.controller

import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.repository.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val repo: UserRepository) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello from User Service!"
    }

    @PostMapping
    fun create(@RequestBody user: User): User = repo.save(user)

    @GetMapping
    fun all(): List<User> = repo.findAll()
}