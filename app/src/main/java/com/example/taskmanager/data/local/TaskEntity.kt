package com.example.taskmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmanager.data.model.Priority
import com.example.taskmanager.data.model.Status

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val taskId: String,
    val title: String,
    val description: String?,
    val deadlineMillis: Long,
    val priority: Priority,
    val status: Status,
    val createdAtMillis: Long,
    val completedAtMillis: Long?,
    val googleCalendarEventId: String?,
    val score: Int
)
