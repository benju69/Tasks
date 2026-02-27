package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateTaskUseCaseImpl @Inject constructor(
    private val repository: TaskRepository,
    private val dispatchers: ICoroutineDispatchers
) : UpdateTaskUseCase {

    override suspend fun invoke(task: Task): Result<Unit> {
        return withContext(dispatchers.io) {
            repository.updateTask(task)
        }
    }
}
