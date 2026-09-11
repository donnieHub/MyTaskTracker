package org.mychko.mytasktracker.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 200)
    var title: String,

    @Column(length = 1000)
    var description: String? = null,

    @Column(nullable = false)
    var userId: Long, // просто внешний ID, без связи через FK на другую БД

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TaskStatus = TaskStatus.OPENED,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
){
    override fun toString(): String {
        return "Task(id=$id, title='$title', description=$description, userId=$userId, status=$status, createdAt=$createdAt)"
    }
}