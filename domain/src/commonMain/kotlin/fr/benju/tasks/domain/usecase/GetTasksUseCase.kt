package fr.benju.tasks.domain.usecase

import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.model.TaskFilter
import kotlinx.coroutines.flow.Flow

interface GetTasksUseCase {
    operator fun invoke(filter: TaskFilter): Flow<List<Task>>
}
