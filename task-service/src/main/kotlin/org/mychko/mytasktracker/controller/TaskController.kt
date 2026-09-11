package org.mychko.mytasktracker.controller

import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import org.mychko.mytasktracker.dto.CreateTaskRequest
import org.mychko.mytasktracker.dto.TaskPatchRequest
import org.mychko.mytasktracker.dto.TaskResponse
import org.mychko.mytasktracker.model.Task
import org.mychko.mytasktracker.service.TasksService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Задачи", description = "API для работы с задачами")
@RestController
@RequestMapping("/tasks")
class TaskController(
    private val service: TasksService
) {

    @Operation(summary = "Создать задачу", description = "Пользователь создает новую задачу")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody task: CreateTaskRequest): TaskResponse {
        return service.create(task).toResponse()
    }

    @Operation(summary = "Получить задачу по ID")
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): TaskResponse =
        service.getTask(id).toResponse()

    @Operation(summary = "Получить все задачи пользователя по userId")
    @GetMapping
    fun getTasksByUserId(@RequestParam userId: Long): List<TaskResponse> =
        service.getTasksByUserId(userId).map { it.toResponse() }

    @Operation(summary = "Обновить задачу пользователя", description = "Частично обновляет данные задачи по ее ID")
    @PatchMapping("/{id}")
    fun patchUser(
        @PathVariable id: Long,
        @RequestBody patch: TaskPatchRequest
    ): TaskResponse =
        service.patch(id, patch).toResponse()

    @Operation(summary = "Удалить задачу пользователя")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long): Unit = service.delete(id)

}

private fun Task.toResponse() = TaskResponse(
    id = id!!,
    title = title,
    description = description,
    userId = userId,
    status = status,
    createdAt = createdAt
)