package fr.benju.tasks.feature.tasklist

import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.model.TaskFilter

data class TaskListViewState(
    val tasks: List<Task> = emptyList(),
    val filter: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = true,
    val error: String? = null,
)
