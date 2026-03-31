package com.taskflow.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.taskflow.data.model.Task
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreTaskSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    private fun tasksCollection(uid: String) =
        firestore.collection("users").document(uid).collection("tasks")

    private val uid: String
        get() = requireNotNull(auth.currentUser?.uid) { "User not authenticated" }

    suspend fun fetchAllTasks(uid: String): List<Task> {
        val snapshot = tasksCollection(uid).get().await()
        return snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
    }

    suspend fun saveTask(task: Task) {
        tasksCollection(uid)
            .document(task.taskId)
            .set(task, SetOptions.merge())
            .await()
    }

    suspend fun deleteTask(taskId: String) {
        tasksCollection(uid)
            .document(taskId)
            .delete()
            .await()
    }
}
