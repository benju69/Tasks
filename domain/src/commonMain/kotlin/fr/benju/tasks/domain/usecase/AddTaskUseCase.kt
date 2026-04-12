package fr.benju.tasks.domain.usecase

import fr.benju.tasks.domain.model.Task

interface AddTaskUseCase {
    suspend operator fun invoke(task: Task): Result<Long>
}
