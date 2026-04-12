package fr.benju.tasks.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.benju.tasks.data.database.TaskDatabase
import fr.benju.tasks.data.mapper.TaskMapper
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val database: TaskDatabase,
    private val taskMapper: TaskMapper,
) : TaskRepository {

    @Volatile
    private var cache: Map<Long, Task> = emptyMap()

    override fun getTasks(): Flow<List<Task>> {
        return database.tasksQueries.getAllTasks()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                val tasks = entities.map { taskMapper.toDomain(it) }
                cache = tasks.associateBy { it.id }
                tasks
            }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return cache[id] ?: database.tasksQueries.getTaskById(id).executeAsOneOrNull()
            ?.let { taskMapper.toDomain(it) }
    }

    override suspend fun addTask(task: Task): Result<Long> {
        return try {
            database.tasksQueries.insertTask(
                title = task.title,
                description = task.description,
                priority = task.priority.name,
                isCompleted = task.isCompleted,
                createdAt = task.createdAt,
            )
            val allTasks = database.tasksQueries.getAllTasks().executeAsList()
            val id = if (allTasks.isEmpty()) -1L else allTasks.maxOf { it.id }
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            database.tasksQueries.updateTask(
                title = task.title,
                description = task.description,
                priority = task.priority.name,
                isCompleted = task.isCompleted,
                id = task.id,
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(id: Long): Result<Unit> {
        return try {
            database.tasksQueries.deleteTask(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
