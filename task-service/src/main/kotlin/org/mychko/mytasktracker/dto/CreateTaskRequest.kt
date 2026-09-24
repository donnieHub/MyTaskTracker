package org.mychko.mytasktracker.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CreateTaskRequest(
    val title: String,

    val description: String?,

    @field:NotNull
    @field:Positive
    val userId: Long?
)