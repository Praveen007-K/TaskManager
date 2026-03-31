package com.taskflow.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.taskflow.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUser(uid: String): User? {
        val snapshot = firestore.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    override suspend fun saveUser(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    override fun isSignedIn(): Boolean = auth.currentUser != null

    override suspend fun signOut() = auth.signOut()
}
