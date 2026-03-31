package com.taskflow.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY deadlineSeconds ASC")
    fun observeAllTasks(): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE statusOrdinal = 0
        AND deadlineSeconds >= :todayStartSeconds
        AND deadlineSeconds < :tomorrowStartSeconds
        ORDER BY deadlineSeconds ASC
    """)
    fun observeTodayTasks(
        todayStartSeconds: Long,
        tomorrowStartSeconds: Long
    ): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE statusOrdinal = 0
        AND deadlineSeconds >= :fromSeconds
        ORDER BY deadlineSeconds ASC
    """)
    fun observeUpcomingTasks(fromSeconds: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE statusOrdinal = 2 ORDER BY deadlineSeconds ASC")
    fun observeOverdueTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE statusOrdinal = 1 ORDER BY completedAtSeconds DESC")
    fun observeCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTasks(tasks: List<TaskEntity>)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTaskById(taskId: String)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}
