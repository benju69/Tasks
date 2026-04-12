package fr.benju.tasks.feature.taskeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.usecase.AddTaskUseCase
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskEditorViewModel(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val dispatchers: ICoroutineDispatchers
) : ViewModel() {

    private val _viewState = MutableStateFlow(TaskEditorViewState())
    val viewState: StateFlow<TaskEditorViewState> = _viewState

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess

    fun loadTask(taskId: Long) {
        viewModelScope.launch(dispatchers.io) {
            val task = getTaskByIdUseCase(taskId) ?: return@launch
            _viewState.value = _viewState.value.copy(
                taskId = task.id,
                title = task.title,
                description = task.description,
                priority = task.priority
            )
        }
    }

    fun updateTitle(title: String) {
        _viewState.value = _viewState.value.copy(title = title)
    }

    fun updateDescription(description: String) {
        _viewState.value = _viewState.value.copy(description = description)
    }

    fun updatePriority(priority: Priority) {
        _viewState.value = _viewState.value.copy(priority = priority)
    }

    fun saveTask() {
        val state = _viewState.value
        if (state.title.isBlank()) {
            _viewState.value = state.copy(error = "Title is required")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _viewState.value = state.copy(isSaving = true)

            val task = Task(
                id = state.taskId ?: 0,
                title = state.title,
                description = state.description,
                priority = state.priority
            )

            val result = if (state.taskId == null) {
                addTaskUseCase(task)
            } else {
                updateTaskUseCase(task)
            }

            result.fold(
                onSuccess = {
                    _saveSuccess.emit(Unit)
                },
                onFailure = {
                    _viewState.value = _viewState.value.copy(
                        isSaving = false,
                        error = "Failed to save the task. Please try again."
                    )
                }
            )
        }
    }
}
