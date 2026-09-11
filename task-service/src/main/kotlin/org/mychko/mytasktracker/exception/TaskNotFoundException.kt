package org.mychko.mytasktracker.exception

class TaskNotFoundException(id: Long) : RuntimeException("Task with id $id not found")