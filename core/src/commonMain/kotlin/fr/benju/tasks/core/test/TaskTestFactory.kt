package fr.benju.tasks.core.test

import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.Task
import kotlinx.datetime.Clock

object TaskTestFactory {

    fun createTask(
        id: Long = 1L,
        title: String = "Test Task",
        description: String = "Test Description",
        priority: Priority = Priority.MEDIUM,
        isCompleted: Boolean = false,
        createdAt: Long = Clock.System.now().toEpochMilliseconds()
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            priority = priority,
            isCompleted = isCompleted,
            createdAt = createdAt
        )
    }

    fun createTasks(count: Int): List<Task> {
        return (1..count).map { index ->
            createTask(
                id = index.toLong(),
                title = "Task $index",
                description = "Description $index",
                priority = Priority.values()[index % 3]
            )
        }
    }
}
