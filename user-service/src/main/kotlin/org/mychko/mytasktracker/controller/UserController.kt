package org.mychko.mytasktracker.controller

import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val service: UserService) {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello from User Service!"
    }

    @PostMapping
    fun create(@RequestBody user: User): User =
        service.create(user)

    @GetMapping
    fun getAll(): List<User> =
        service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): User =
        service.getById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User =
        service.update(id, user)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) =
        service.delete(id)
}