package org.mychko.mytasktracker.dto

import jakarta.validation.constraints.NotNull

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    @field:NotNull
    val userId: Long?
)