package org.mychko.mytasktracker.controller

import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Пользователи", description = "API для работы с пользователями")
@RestController
@RequestMapping("/users")
class UserController(private val service: UserService) {

    @Operation(summary = "Тестовый метод", description = "Возвращает простое приветствие")
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello from User Service!"
    }

    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя в системе")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody user: User): User {
        return service.create(user)
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    fun getAll(): List<User> =
        service.getAll()

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): User =
        service.getById(id)

    @Operation(summary = "Обновить пользователя", description = "Полностью обновляет данные пользователя по его ID")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User =
        service.update(id, user)

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}