package fr.benju.tasks.feature.tasklist

import app.cash.turbine.test
import fr.benju.tasks.core.test.CoroutineTestExtension
import fr.benju.tasks.core.test.TaskTestFactory
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.domain.service.ReminderScheduler
import fr.benju.tasks.domain.usecase.DeleteTaskUseCase
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import fr.benju.tasks.domain.usecase.ToggleTaskStatusUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TaskListViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutineExtension = CoroutineTestExtension()

    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val toggleTaskStatusUseCase: ToggleTaskStatusUseCase = mockk()
    private val deleteTaskUseCase: DeleteTaskUseCase = mockk()
    private val reminderScheduler: ReminderScheduler = mockk {
        every { schedule(any(), any(), any()) } just Runs
        every { cancel(any()) } just Runs
    }

    @Test
    fun `init should load tasks`() = runTest {
        // Given
        val tasks = TaskTestFactory.createTasks(3)
        every { getTasksUseCase(TaskFilter.ALL) } returns flowOf(tasks)

        // When
        val viewModel = TaskListViewModel(
            getTasksUseCase,
            toggleTaskStatusUseCase,
            deleteTaskUseCase,
            reminderScheduler
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
            deleteTaskUseCase,
            reminderScheduler
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
            deleteTaskUseCase,
            reminderScheduler
        )

        // When
        viewModel.onDeleteTask(1L)

        // Then
        coVerify { deleteTaskUseCase(1L) }
    }
}
