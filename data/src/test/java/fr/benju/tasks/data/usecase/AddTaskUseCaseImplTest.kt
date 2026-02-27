package fr.benju.tasks.data.usecase

import fr.benju.tasks.core.test.CoroutineTestExtension
import fr.benju.tasks.core.test.TaskTestFactory
import fr.benju.tasks.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AddTaskUseCaseImplTest {

    @JvmField
    @RegisterExtension
    val coroutineExtension = CoroutineTestExtension()

    private val repository: TaskRepository = mockk()
    private val useCase = AddTaskUseCaseImpl(repository, coroutineExtension.dispatchers)

    @Test
    fun `invoke should call repository and return success with id`() = runTest {
        // Given
        val task = TaskTestFactory.createTask()
        coEvery { repository.addTask(task) } returns Result.success(1L)

        // When
        val result = useCase(task)

        // Then
        result.isSuccess shouldBeEqualTo true
        result.getOrNull() shouldBeEqualTo 1L
        coVerify { repository.addTask(task) }
    }
}
