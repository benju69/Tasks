package fr.benju.tasks.data.mapper

import fr.benju.tasks.data.database.entity.TaskEntity
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.RepeatInterval
import fr.benju.tasks.domain.model.Task
import javax.inject.Inject

class TaskMapper @Inject constructor() {

    fun toDomain(entity: TaskEntity): Task {
        return Task(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            priority = Priority.valueOf(entity.priority),
            isCompleted = entity.isCompleted,
            createdAt = entity.createdAt,
            dueDate = entity.dueDate,
            repeatInterval = runCatching { RepeatInterval.valueOf(entity.repeatInterval) }
                .getOrDefault(RepeatInterval.NONE)
        )
    }

    fun toEntity(domain: Task): TaskEntity {
        return TaskEntity(
            id = domain.id,
            title = domain.title,
            description = domain.description,
            priority = domain.priority.name,
            isCompleted = domain.isCompleted,
            createdAt = domain.createdAt,
            dueDate = domain.dueDate,
            repeatInterval = domain.repeatInterval.name
        )
    }
}
