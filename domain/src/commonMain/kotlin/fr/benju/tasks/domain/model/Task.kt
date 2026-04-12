package fr.benju.tasks.domain.model

import kotlinx.datetime.Clock

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)
