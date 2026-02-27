package fr.benju.tasks.feature.tasklist

import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.model.TaskFilter

data class TaskListViewState(
    var tasks: List<Task> = mutableListOf(),
    val filter: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = true,
    val error: String? = null,
)
