package com.example.taskmanager.data.local

import androidx.room.TypeConverter
import com.example.taskmanager.data.model.Priority
import com.example.taskmanager.data.model.Status

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter
    fun fromStatus(status: Status): String = status.name

    @TypeConverter
    fun toStatus(value: String): Status = Status.valueOf(value)
}
