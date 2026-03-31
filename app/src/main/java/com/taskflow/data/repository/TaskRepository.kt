package com.taskflow.data.repository

import com.taskflow.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTodayTasks(todayStart: Long, tomorrowStart: Long): Flow<List<Task>>
    fun observeUpcomingTasks(fromSeconds: Long): Flow<List<Task>>
    fun observeOverdueTasks(): Flow<List<Task>>
    fun observeCompletedTasks(): Flow<List<Task>>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
    suspend fun syncFromFirestore(uid: String)
}
