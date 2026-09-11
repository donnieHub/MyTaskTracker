package org.mychko.mytasktracker.dto

import org.mychko.mytasktracker.model.TaskStatus
import java.time.LocalDateTime

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val userId: Long,
    val status: TaskStatus,
    val createdAt: LocalDateTime
)