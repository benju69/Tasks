package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTasksUseCaseImpl @Inject constructor(
    private val repository: TaskRepository,
    private val dispatchers: ICoroutineDispatchers
) : GetTasksUseCase {

    override fun invoke(filter: TaskFilter): Flow<List<Task>> {
        return repository.getTasks()
            .map { tasks -> applyFilter(tasks, filter) }
            .flowOn(dispatchers.io)
    }

    private fun applyFilter(tasks: List<Task>, filter: TaskFilter): List<Task> {
        return when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.ACTIVE -> tasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }
    }
}
