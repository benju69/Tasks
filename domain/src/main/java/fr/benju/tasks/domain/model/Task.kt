package fr.benju.tasks.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,
)
