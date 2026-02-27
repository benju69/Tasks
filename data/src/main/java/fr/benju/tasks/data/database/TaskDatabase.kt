package fr.benju.tasks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.benju.tasks.data.database.dao.TaskDao
import fr.benju.tasks.data.database.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "task_manager_db"
    }
}
