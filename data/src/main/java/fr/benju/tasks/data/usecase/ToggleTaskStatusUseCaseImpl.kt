package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.usecase.ToggleTaskStatusUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleTaskStatusUseCaseImpl @Inject constructor(
    private val repository: TaskRepository,
    private val dispatchers: ICoroutineDispatchers
) : ToggleTaskStatusUseCase {

    override suspend fun invoke(taskId: Long): Result<Unit> {
        return withContext(dispatchers.io) {
            try {
                val task = repository.getTaskById(taskId)
                if (task != null) {
                    val updatedTask = task.copy(isCompleted = !task.isCompleted)
                    repository.updateTask(updatedTask)
                } else {
                    Result.failure(Exception("Task not found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
