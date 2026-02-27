package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.usecase.DeleteTaskUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteTaskUseCaseImpl @Inject constructor(
    private val repository: TaskRepository,
    private val dispatchers: ICoroutineDispatchers
) : DeleteTaskUseCase {

    override suspend fun invoke(taskId: Long): Result<Unit> {
        return withContext(dispatchers.io) {
            repository.deleteTask(taskId)
        }
    }
}
