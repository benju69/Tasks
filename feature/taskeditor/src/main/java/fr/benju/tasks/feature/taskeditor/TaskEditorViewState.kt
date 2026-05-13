package fr.benju.tasks.feature.taskeditor

import androidx.annotation.StringRes
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.RepeatInterval

data class TaskEditorViewState(
    val taskId: Long? = null,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    /** Epoch-ms combining the chosen local date + time (not UTC midnight). Null = no due date. */
    val dueDate: Long? = null,
    /** Epoch-day (LocalDate.toEpochDay()) selected in DatePicker, waiting for time to be chosen. */
    val pendingDateEpochDay: Long? = null,
    val repeatInterval: RepeatInterval = RepeatInterval.NONE,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    /** Set to true after the user picks a date+time; consumed by the UI to request POST_NOTIFICATIONS. */
    val requestNotificationPermission: Boolean = false,
    val isSaving: Boolean = false,
    @StringRes val error: Int? = null
)
