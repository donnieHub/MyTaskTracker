package org.mychko.mytasktracker.repository

import org.mychko.mytasktracker.model.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {
    fun findByUserId(userId: Long): List<Task>
}