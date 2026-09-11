package org.mychko.mytasktracker.exception

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ApiResponse(
        responseCode = "404",
        description = "Задача не найдена",
        content = [Content(
            schema = Schema(
                implementation = ErrorResponse::class
            )
        )]
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFound(ex: TaskNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Task not found: {}", ex.message)

        val body = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message ?: "Task not found",
            timestamp = Instant.now()
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }
}