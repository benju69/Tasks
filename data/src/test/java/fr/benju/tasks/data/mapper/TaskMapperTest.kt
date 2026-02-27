package fr.benju.tasks.data.mapper

import fr.benju.tasks.data.database.entity.TaskEntity
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.Task
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class TaskMapperTest {

    private val mapper = TaskMapper()

    @Test
    fun `toDomain should map entity to domain model correctly`() {
        val entity = TaskEntity(
            id = 1L,
            title = "Test Task",
            description = "Test Description",
            priority = "HIGH",
            isCompleted = false,
            createdAt = 123456789L
        )

        val result = mapper.toDomain(entity)

        result.id shouldBeEqualTo 1L
        result.title shouldBeEqualTo "Test Task"
        result.description shouldBeEqualTo "Test Description"
        result.priority shouldBeEqualTo Priority.HIGH
        result.isCompleted shouldBeEqualTo false
        result.createdAt shouldBeEqualTo 123456789L
    }

    @Test
    fun `toEntity should map domain to entity correctly`() {
        val domain = Task(
            id = 1L,
            title = "Test Task",
            description = "Test Description",
            priority = Priority.MEDIUM,
            isCompleted = true,
            createdAt = 987654321L
        )

        val result = mapper.toEntity(domain)

        result.id shouldBeEqualTo 1L
        result.title shouldBeEqualTo "Test Task"
        result.description shouldBeEqualTo "Test Description"
        result.priority shouldBeEqualTo "MEDIUM"
        result.isCompleted shouldBeEqualTo true
        result.createdAt shouldBeEqualTo 987654321L
    }
}
