package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.test.CoroutineTestExtension
import fr.benju.tasks.core.test.TaskTestFactory
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.domain.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class GetTasksUseCaseImplTest {

    @JvmField
    @RegisterExtension
    val coroutineExtension = CoroutineTestExtension()

    private val repository: TaskRepository = mockk()
    private val useCase = GetTasksUseCaseImpl(repository, coroutineExtension.dispatchers)

    @Test
    fun `invoke with ALL filter should return all tasks`() = runTest {
        // Given
        val tasks = TaskTestFactory.createTasks(3)
        every { repository.getTasks() } returns flowOf(tasks)

        // When
        val result = useCase(TaskFilter.ALL).first()

        // Then
        result.size shouldBeEqualTo 3
    }

    @Test
    fun `invoke with ACTIVE filter should return only active tasks`() = runTest {
        // Given
        val tasks = listOf(
            TaskTestFactory.createTask(id = 1, isCompleted = false),
            TaskTestFactory.createTask(id = 2, isCompleted = true),
            TaskTestFactory.createTask(id = 3, isCompleted = false)
        )
        every { repository.getTasks() } returns flowOf(tasks)

        // When
        val result = useCase(TaskFilter.ACTIVE).first()

        // Then
        result.size shouldBeEqualTo 2
        result.all { !it.isCompleted } shouldBeEqualTo true
    }

    @Test
    fun `invoke with COMPLETED filter should return only completed tasks`() = runTest {
        // Given
        val tasks = listOf(
            TaskTestFactory.createTask(id = 1, isCompleted = false),
            TaskTestFactory.createTask(id = 2, isCompleted = true),
            TaskTestFactory.createTask(id = 3, isCompleted = true)
        )
        every { repository.getTasks() } returns flowOf(tasks)

        // When
        val result = useCase(TaskFilter.COMPLETED).first()

        // Then
        result.size shouldBeEqualTo 2
        result.all { it.isCompleted } shouldBeEqualTo true
    }
}
