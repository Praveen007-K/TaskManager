package com.example.taskmanager.data.repository

import com.example.taskmanager.data.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun getCurrentUser(): FirebaseUser?
    fun isLoggedIn(): Boolean
    suspend fun saveUserToFirestore(user: User)
}
