package fr.benju.tasks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.benju.tasks.data.database.dao.TaskDao
import fr.benju.tasks.data.database.entity.TaskEntity
import fr.benju.tasks.data.database.migration.MIGRATION_1_2
import fr.benju.tasks.data.database.migration.MIGRATION_2_3

@Database(
    entities = [TaskEntity::class],
    version = 3,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "task_manager_db"
        val migrations = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
    }
}
