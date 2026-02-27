package fr.benju.tasks.feature.taskeditor

import fr.benju.tasks.domain.model.Priority

data class TaskEditorViewState(
    val taskId: Long? = null,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val isSaving: Boolean = false,
    val error: String? = null
)
