package org.mychko.mytasktracker.dto

data class UserBatchEvent(
    val userIds: List<Long>,
    val eventType: UserEventType
)
