package fr.benju.tasks.feature.tasklist

import app.cash.turbine.test
import fr.benju.tasks.core.test.TaskTestFactory
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.domain.usecase.DeleteTaskUseCase
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import fr.benju.tasks.domain.usecase.ToggleTaskStatusUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class TaskListViewModelTest {

    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val toggleTaskStatusUseCase: ToggleTaskStatusUseCase = mockk()
    private val deleteTaskUseCase: DeleteTaskUseCase = mockk()

    @Test
    fun `init should load tasks`() = runTest {
        // Given
        val tasks = TaskTestFactory.createTasks(3)
        every { getTasksUseCase(TaskFilter.ALL) } returns flowOf(tasks)

        // When
        val viewModel = TaskListViewModel(
            getTasksUseCase,
            toggleTaskStatusUseCase,
            deleteTaskUseCase
        )

        // Then
        viewModel.viewState.test {
            val state = awaitItem()
            state.tasks.size shouldBeEqualTo 3
            state.isLoading shouldBeEqualTo false
        }
    }

    @Test
    fun `onTaskToggled should call toggle use case`() = runTest {
        // Given
        val tasks = TaskTestFactory.createTasks(1)
        every { getTasksUseCase(TaskFilter.ALL) } returns flowOf(tasks)
        coEvery { toggleTaskStatusUseCase(any()) } returns Result.success(Unit)

        val viewModel = TaskListViewModel(
            getTasksUseCase,
            toggleTaskStatusUseCase,
            deleteTaskUseCase
        )

        // When
        viewModel.onTaskToggled(1L)

        // Then
        coVerify { toggleTaskStatusUseCase(1L) }
    }

    @Test
    fun `onDeleteTask should call delete use case`() = runTest {
        // Given
        val tasks = TaskTestFactory.createTasks(1)
        every { getTasksUseCase(TaskFilter.ALL) } returns flowOf(tasks)
        coEvery { deleteTaskUseCase(any()) } returns Result.success(Unit)

        val viewModel = TaskListViewModel(
            getTasksUseCase,
            toggleTaskStatusUseCase,
            deleteTaskUseCase
        )

        // When
        viewModel.onDeleteTask(1L)

        // Then
        coVerify { deleteTaskUseCase(1L) }
    }
}
