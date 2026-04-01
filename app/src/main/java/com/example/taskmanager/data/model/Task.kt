package com.example.taskmanager.data.model

import com.google.firebase.Timestamp

enum class Priority { LOW, MEDIUM, HIGH, CRITICAL }

enum class Status { PENDING, DONE, OVERDUE }

data class Task(
    val taskId: String = "",
    val title: String = "",
    val description: String? = null,
    val deadline: Timestamp = Timestamp.now(),
    val priority: Priority = Priority.MEDIUM,
    val status: Status = Status.PENDING,
    val createdAt: Timestamp = Timestamp.now(),
    val completedAt: Timestamp? = null,
    val googleCalendarEventId: String? = null,
    val score: Int = 0
)
