package org.mychko.mytasktracker.exception

class UserNotFoundException(id: Long) : RuntimeException("User with id $id not found")