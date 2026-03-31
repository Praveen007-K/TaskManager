package com.taskflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskflow.data.model.Priority
import com.taskflow.data.model.Status
import com.taskflow.data.model.Task
import com.google.firebase.Timestamp

/**
 * Room entity — local cache of a [Task].
 *
 * Table: tasks
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val taskId: String,
    val title: String,
    val description: String?,
    val deadlineSeconds: Long,       // Timestamp.seconds
    val priorityOrdinal: Int,        // Priority.ordinal
    val statusOrdinal: Int,          // Status.ordinal
    val createdAtSeconds: Long,
    val completedAtSeconds: Long?,
    val googleCalendarEventId: String?,
    val score: Int
)

// ── Mappers ────────────────────────────────────────────────────────────────────

fun TaskEntity.toDomain(): Task = Task(
    taskId                = taskId,
    title                 = title,
    description           = description,
    deadline              = Timestamp(deadlineSeconds, 0),
    priority              = Priority.entries[priorityOrdinal],
    status                = Status.entries[statusOrdinal],
    createdAt             = Timestamp(createdAtSeconds, 0),
    completedAt           = completedAtSeconds?.let { Timestamp(it, 0) },
    googleCalendarEventId = googleCalendarEventId,
    score                 = score
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    taskId                = taskId,
    title                 = title,
    description           = description,
    deadlineSeconds       = deadline.seconds,
    priorityOrdinal       = priority.ordinal,
    statusOrdinal         = status.ordinal,
    createdAtSeconds      = createdAt.seconds,
    completedAtSeconds    = completedAt?.seconds,
    googleCalendarEventId = googleCalendarEventId,
    score                 = score
)
