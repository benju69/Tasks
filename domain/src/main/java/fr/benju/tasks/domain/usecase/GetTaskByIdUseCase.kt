package fr.benju.tasks.domain.usecase

import fr.benju.tasks.domain.model.Task

interface GetTaskByIdUseCase {
    suspend operator fun invoke(id: Long): Task?
}

