package fr.benju.tasks.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val priority: String, // "LOW", "MEDIUM", "HIGH"
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
