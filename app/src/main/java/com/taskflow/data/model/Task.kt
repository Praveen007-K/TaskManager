package com.taskflow.data.model

import com.google.firebase.Timestamp

/**
 * Domain model for a task.
 *
 * Maps to Firestore path: users/{uid}/tasks/{taskId}
 * Also round-trips through [TaskEntity] for Room local cache.
 */
data class Task(
    val taskId: String = "",
    val title: String = "",                       // required, max 100 chars
    val description: String? = null,              // optional, max 500 chars
    val deadline: Timestamp = Timestamp.now(),    // must be in the future on creation
    val priority: Priority = Priority.MEDIUM,
    val status: Status = Status.PENDING,
    val createdAt: Timestamp = Timestamp.now(),
    val completedAt: Timestamp? = null,           // null while pending/overdue
    val googleCalendarEventId: String? = null,    // linked Calendar event
    val score: Int = 0                            // awarded on completion
)
