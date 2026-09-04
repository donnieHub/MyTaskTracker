package org.mychko.mytasktracker.dto

data class UserPatchRequest(
    val username: String? = null,
    val email: String? = null,
    val isActive: Boolean? = null
)