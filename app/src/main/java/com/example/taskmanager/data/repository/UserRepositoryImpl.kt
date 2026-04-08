package com.example.taskmanager.data.repository

import com.example.taskmanager.data.model.User
import com.example.taskmanager.data.remote.AuthDataSource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override fun getCurrentUser(): FirebaseUser? = authDataSource.getCurrentUser()

    override fun isLoggedIn(): Boolean = authDataSource.isLoggedIn()

    override suspend fun saveUserToFirestore(user: User) {
        firestore
            .collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }
}
