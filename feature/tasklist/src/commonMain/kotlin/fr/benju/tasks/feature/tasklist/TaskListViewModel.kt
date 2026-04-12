package fr.benju.tasks.feature.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.domain.usecase.DeleteTaskUseCase
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import fr.benju.tasks.domain.usecase.ToggleTaskStatusUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val toggleTaskStatusUseCase: ToggleTaskStatusUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(TaskListViewState())
    val viewState: StateFlow<TaskListViewState> = _viewState

    private val _filter = MutableStateFlow(TaskFilter.ALL)

    init {
        viewModelScope.launch {
            _filter
                .flatMapLatest { filter ->
                    _viewState.value = _viewState.value.copy(isLoading = true)
                    getTasksUseCase(filter)
                }
                .collect { tasks ->
                    _viewState.value = _viewState.value.copy(
                        tasks = tasks,
                        isLoading = false
                    )
                }
        }
    }

    fun applyFilter(filter: TaskFilter) {
        _viewState.value = _viewState.value.copy(filter = filter)
        _filter.value = filter
    }

    fun onTaskToggled(taskId: Long) {
        viewModelScope.launch {
            toggleTaskStatusUseCase(taskId).onFailure { error ->
                _viewState.value = _viewState.value.copy(error = error.message)
            }
        }
    }

    fun onDeleteTask(taskId: Long) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId).onFailure { error ->
                _viewState.value = _viewState.value.copy(error = error.message)
            }
        }
    }

    fun clearError() {
        _viewState.value = _viewState.value.copy(error = null)
    }
}
