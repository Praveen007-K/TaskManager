package com.taskflow.data.repository

import com.taskflow.data.local.TaskDao
import com.taskflow.data.local.toDomain
import com.taskflow.data.local.toEntity
import com.taskflow.data.model.Task
import com.taskflow.data.remote.FirestoreTaskSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val firestoreSource: FirestoreTaskSource
) : TaskRepository {

    override fun observeTodayTasks(todayStart: Long, tomorrowStart: Long): Flow<List<Task>> =
        taskDao.observeTodayTasks(todayStart, tomorrowStart).map { list -> list.map { it.toDomain() } }

    override fun observeUpcomingTasks(fromSeconds: Long): Flow<List<Task>> =
        taskDao.observeUpcomingTasks(fromSeconds).map { list -> list.map { it.toDomain() } }

    override fun observeOverdueTasks(): Flow<List<Task>> =
        taskDao.observeOverdueTasks().map { list -> list.map { it.toDomain() } }

    override fun observeCompletedTasks(): Flow<List<Task>> =
        taskDao.observeCompletedTasks().map { list -> list.map { it.toDomain() } }

    override suspend fun getTaskById(taskId: String): Task? =
        taskDao.getTaskById(taskId)?.toDomain()

    override suspend fun addTask(task: Task) {
        taskDao.upsertTask(task.toEntity())
        firestoreSource.saveTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.upsertTask(task.toEntity())
        firestoreSource.saveTask(task)
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.deleteTaskById(taskId)
        firestoreSource.deleteTask(taskId)
    }

    override suspend fun syncFromFirestore(uid: String) {
        val remoteTasks = firestoreSource.fetchAllTasks(uid)
        taskDao.upsertTasks(remoteTasks.map { it.toEntity() })
    }
}
