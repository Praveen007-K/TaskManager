package com.taskflow.data.repository

import com.taskflow.data.model.User

interface UserRepository {
    suspend fun getUser(uid: String): User?
    suspend fun saveUser(user: User)
    fun getCurrentUserId(): String?
    fun isSignedIn(): Boolean
    suspend fun signOut()
}
