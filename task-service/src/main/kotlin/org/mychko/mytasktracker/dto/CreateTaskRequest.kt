package org.mychko.mytasktracker.dto

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    val userId: Long
)