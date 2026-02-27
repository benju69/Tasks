package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.usecase.AddTaskUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddTaskUseCaseImpl @Inject constructor(
    private val repository: TaskRepository,
    private val dispatchers: ICoroutineDispatchers
) : AddTaskUseCase {

    override suspend fun invoke(task: Task): Result<Long> {
        return withContext(dispatchers.io) {
            repository.addTask(task)
        }
    }
}
