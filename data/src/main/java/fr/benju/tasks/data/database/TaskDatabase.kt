package fr.benju.tasks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.benju.tasks.data.database.dao.TaskDao
import fr.benju.tasks.data.database.entity.TaskEntity
import fr.benju.tasks.data.database.migration.MIGRATION_1_2

@Database(
    entities = [TaskEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "task_manager_db"
        val migrations = arrayOf(MIGRATION_1_2)
    }
}
