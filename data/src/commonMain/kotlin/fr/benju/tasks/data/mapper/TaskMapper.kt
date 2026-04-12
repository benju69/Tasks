package fr.benju.tasks.data.mapper

import fr.benju.tasks.data.database.TaskEntity
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.Task

class TaskMapper {
    fun toDomain(entity: TaskEntity): Task {
        return Task(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            priority = Priority.valueOf(entity.priority),
            isCompleted = entity.isCompleted,
            createdAt = entity.createdAt
        )
    }
}
