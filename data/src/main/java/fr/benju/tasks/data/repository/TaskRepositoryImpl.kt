package fr.benju.tasks.data.repository

import fr.benju.tasks.data.database.dao.TaskDao
import fr.benju.tasks.data.mapper.TaskMapper
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskMapper: TaskMapper,
) : TaskRepository {

    private val cache = mutableListOf<Task>()

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            val tasks = entities.map { taskMapper.toDomain(it) }
            cache.clear()
            cache.addAll(tasks)
            tasks
        }
    }

    override suspend fun getTaskById(id: Long): Task? {
        val cachedTask = cache.find { it.id == id }
        if (cachedTask != null) {
            return cachedTask
        }

        return taskDao.getTaskById(id)?.let { taskMapper.toDomain(it) }
    }

    override suspend fun addTask(task: Task): Result<Long> {
        return try {
            val id = taskDao.insertTask(taskMapper.toEntity(task))
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            taskDao.updateTask(taskMapper.toEntity(task))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(id: Long): Result<Unit> {
        return try {
            taskDao.deleteTask(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
