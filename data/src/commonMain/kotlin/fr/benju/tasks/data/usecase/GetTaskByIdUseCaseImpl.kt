package fr.benju.tasks.data.usecase

import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase

class GetTaskByIdUseCaseImpl(
    private val repository: TaskRepository
) : GetTaskByIdUseCase {
    override suspend fun invoke(id: Long): Task? {
        return repository.getTaskById(id)
    }
}
