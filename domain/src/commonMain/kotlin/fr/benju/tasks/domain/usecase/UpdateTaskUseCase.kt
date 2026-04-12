package fr.benju.tasks.domain.usecase

import fr.benju.tasks.domain.model.Task

interface UpdateTaskUseCase {
    suspend operator fun invoke(task: Task): Result<Unit>
}
