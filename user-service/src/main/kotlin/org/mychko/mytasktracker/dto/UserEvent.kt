package org.mychko.mytasktracker.dto

import org.mychko.mytasktracker.model.User

data class UserEvent(
    val userId: Long,
    val eventType: UserEventType,
    val user: User?
)
