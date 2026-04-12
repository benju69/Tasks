package fr.benju.tasks.feature.taskeditor

import androidx.annotation.StringRes
import fr.benju.tasks.domain.model.Priority

data class TaskEditorViewState(
    val taskId: Long? = null,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val showDatePicker: Boolean = false,
    val isSaving: Boolean = false,
    @StringRes val error: Int? = null
)
