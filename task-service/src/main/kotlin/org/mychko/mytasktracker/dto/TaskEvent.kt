package org.mychko.mytasktracker.dto

import org.mychko.mytasktracker.model.Task

data class TaskEvent(
    val taskId: Long,
    val eventType: TaskEventType,
    val task: Task?
)
