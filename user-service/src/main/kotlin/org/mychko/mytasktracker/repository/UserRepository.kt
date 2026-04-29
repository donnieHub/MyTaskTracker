package org.mychko.mytasktracker.repository

import org.mychko.mytasktracker.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>