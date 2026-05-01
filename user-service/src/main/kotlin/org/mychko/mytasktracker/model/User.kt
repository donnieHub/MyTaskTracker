package org.mychko.mytasktracker.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    var userName: String,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)