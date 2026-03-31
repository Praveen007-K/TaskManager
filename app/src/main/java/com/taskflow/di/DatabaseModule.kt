package com.taskflow.di

import android.content.Context
import androidx.room.Room
import com.taskflow.data.local.TaskDao
import com.taskflow.data.local.TaskManagerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskManagerDatabase =
        Room.databaseBuilder(
            context,
            TaskManagerDatabase::class.java,
            "taskmanager.db"
        ).build()

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskManagerDatabase): TaskDao = db.taskDao()
}
