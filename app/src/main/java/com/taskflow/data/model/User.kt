package com.taskflow.data.model

/**
 * Domain model for the authenticated user.
 *
 * Maps to Firestore path: users/{uid}
 */
data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoURL: String = ""
)
