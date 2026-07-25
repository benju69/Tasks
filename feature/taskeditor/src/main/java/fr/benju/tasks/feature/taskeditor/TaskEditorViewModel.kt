package fr.benju.tasks.feature.taskeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.RepeatInterval
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.service.ReminderScheduler
import fr.benju.tasks.domain.usecase.AddTaskUseCase
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val dispatchers: ICoroutineDispatchers,
    private val reminderScheduler: ReminderScheduler
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
                priority = task.priority,
                dueDate = task.dueDate,
                repeatInterval = task.repeatInterval
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

    fun updateRepeatInterval(interval: RepeatInterval) {
        _viewState.value = _viewState.value.copy(repeatInterval = interval)
    }

    // ── Date picker ──────────────────────────────────────────────────────────

    fun showDatePicker() {
        _viewState.value = _viewState.value.copy(showDatePicker = true)
    }

    fun hideDatePicker() {
        _viewState.value = _viewState.value.copy(showDatePicker = false)
    }

    /**
     * Called when the user confirms a date in the DatePickerDialog.
     * [utcDateMs] is Material3 DatePickerState.selectedDateMillis (UTC midnight).
     * We extract a LocalDate in UTC (time-zone agnostic) and store its epochDay,
     * then immediately show the time picker.
     */
    fun onDateSelected(utcDateMs: Long?) {
        if (utcDateMs == null) {
            _viewState.value = _viewState.value.copy(showDatePicker = false)
            return
        }
        val epochDay = utcDateMs / MILLIS_PER_DAY
        _viewState.value = _viewState.value.copy(
            showDatePicker = false,
            showTimePicker = true,
            pendingDateEpochDay = epochDay
        )
    }

    // ── Time picker ──────────────────────────────────────────────────────────

    fun hideTimePicker() {
        _viewState.value = _viewState.value.copy(showTimePicker = false, pendingDateEpochDay = null)
    }

    /**
     * Called when the user confirms a time. Combines [pendingDateEpochDay] + hour/minute
     * in the system time zone to produce the final due-date epoch-ms.
     * If dismissed without confirmation, falls back to 09:00 AM.
     */
    fun onTimeSelected(hour: Int, minute: Int) {
        val epochDay = _viewState.value.pendingDateEpochDay ?: return
        val dueDate = combineDateAndTime(epochDay, hour, minute)
        _viewState.value = _viewState.value.copy(
            dueDate = dueDate,
            showTimePicker = false,
            pendingDateEpochDay = null,
            requestNotificationPermission = true
        )
    }

    /** Called by the UI once the notification-permission request has been launched. */
    fun onNotificationPermissionHandled() {
        _viewState.value = _viewState.value.copy(requestNotificationPermission = false)
    }

    /** User dismissed the time picker → use 09:00 AM on the pending date. */
    fun onTimePickerDismissed() {
        val epochDay = _viewState.value.pendingDateEpochDay
        if (epochDay != null) {
            onTimeSelected(DEFAULT_HOUR, DEFAULT_MINUTE)
        } else {
            _viewState.value = _viewState.value.copy(showTimePicker = false)
        }
    }

    fun clearDueDate() {
        _viewState.value = _viewState.value.copy(dueDate = null)
    }

    // ── Save ─────────────────────────────────────────────────────────────────

    fun saveTask() {
        val state = _viewState.value
        if (state.title.isBlank()) {
            _viewState.value = state.copy(error = R.string.task_editor_error_title_required)
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _viewState.value = state.copy(isSaving = true)

            val task = Task(
                id = state.taskId ?: 0,
                title = state.title,
                description = state.description,
                priority = state.priority,
                dueDate = state.dueDate,
                repeatInterval = state.repeatInterval
            )

            if (state.taskId == null) {
                addTaskUseCase(task).fold(
                    onSuccess = { newId ->
                        state.dueDate?.let {
                            reminderScheduler.schedule(newId, state.title, it, state.repeatInterval)
                        }
                        _saveSuccess.emit(Unit)
                    },
                    onFailure = {
                        _viewState.value = _viewState.value.copy(
                            isSaving = false,
                            error = R.string.task_editor_error_save_failed
                        )
                    }
                )
            } else {
                updateTaskUseCase(task).fold(
                    onSuccess = {
                        reminderScheduler.cancel(state.taskId)
                        state.dueDate?.let {
                            reminderScheduler.schedule(state.taskId, state.title, it, state.repeatInterval)
                        }
                        _saveSuccess.emit(Unit)
                    },
                    onFailure = {
                        _viewState.value = _viewState.value.copy(
                            isSaving = false,
                            error = R.string.task_editor_error_save_failed
                        )
                    }
                )
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun combineDateAndTime(epochDay: Long, hour: Int, minute: Int): Long {
        return LocalDate.ofEpochDay(epochDay)
            .atTime(hour, minute)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    companion object {
        private const val MILLIS_PER_DAY = 86_400_000L
        const val DEFAULT_HOUR = 9
        const val DEFAULT_MINUTE = 0
    }
}
