package org.mychko.mytasktracker.dto

import org.mychko.mytasktracker.model.TaskStatus

data class TaskPatchRequest(
    val title: String? = null,
    val description: String? = null,
    val status: TaskStatus? = null
)