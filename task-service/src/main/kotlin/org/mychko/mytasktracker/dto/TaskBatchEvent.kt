package org.mychko.mytasktracker.dto

data class TaskBatchEvent(
    val taskIds: List<Long>,
    val eventType: TaskEventType
)
