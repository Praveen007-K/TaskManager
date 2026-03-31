package com.taskflow.data.model

/**
 * Represents a single score history entry.
 *
 * Maps to Firestore path: users/{uid}/scoreHistory/{entryId}
 */
data class ScoreEntry(
    val entryId: String = "",
    val taskId: String = "",
    val taskTitle: String = "",
    val points: Int = 0,
    val reason: String = "",       // e.g. "Completed on time — High"
    val earnedAt: Long = 0L        // epoch millis for easy sorting
)
