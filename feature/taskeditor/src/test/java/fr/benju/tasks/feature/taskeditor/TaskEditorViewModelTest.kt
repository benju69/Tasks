package fr.benju.tasks.feature.taskeditor

import app.cash.turbine.test
import fr.benju.tasks.core.test.CoroutineTestExtension
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.service.ReminderScheduler
import fr.benju.tasks.domain.usecase.AddTaskUseCase
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TaskEditorViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutineExtension = CoroutineTestExtension()

    private val addTaskUseCase: AddTaskUseCase = mockk()
    private val updateTaskUseCase: UpdateTaskUseCase = mockk()
    private val getTaskByIdUseCase: GetTaskByIdUseCase = mockk()
    private val reminderScheduler: ReminderScheduler = mockk {
        every { schedule(any(), any(), any()) } just Runs
        every { cancel(any()) } just Runs
    }

    @Test
    fun `updateTitle should update view state`() = runTest {
        // Given
        val viewModel = TaskEditorViewModel(
            addTaskUseCase,
            updateTaskUseCase,
            getTaskByIdUseCase,
            coroutineExtension.dispatchers,
            reminderScheduler
        )

        // When
        viewModel.updateTitle("New Task")

        // Then
        viewModel.viewState.value.title shouldBeEqualTo "New Task"
    }

    @Test
    fun `updatePriority should update view state`() = runTest {
        // Given
        val viewModel = TaskEditorViewModel(
            addTaskUseCase,
            updateTaskUseCase,
            getTaskByIdUseCase,
            coroutineExtension.dispatchers,
            reminderScheduler
        )

        // When
        viewModel.updatePriority(Priority.HIGH)

        // Then
        viewModel.viewState.value.priority shouldBeEqualTo Priority.HIGH
    }

    @Test
    fun `saveTask should call addTaskUseCase for new task`() = runTest {
        // Given
        coEvery { addTaskUseCase(any()) } returns Result.success(1L)

        val viewModel = TaskEditorViewModel(
            addTaskUseCase,
            updateTaskUseCase,
            getTaskByIdUseCase,
            coroutineExtension.dispatchers,
            reminderScheduler
        )
        viewModel.updateTitle("Test Task")

        // When
        viewModel.saveTask()

        // Then
        coVerify { addTaskUseCase(any()) }
    }

    @Test
    fun `saveTask should emit success event on successful save`() = runTest {
        // Given
        coEvery { addTaskUseCase(any()) } returns Result.success(1L)

        val viewModel = TaskEditorViewModel(
            addTaskUseCase,
            updateTaskUseCase,
            getTaskByIdUseCase,
            coroutineExtension.dispatchers,
            reminderScheduler
        )
        viewModel.updateTitle("Test Task")

        // When / Then
        viewModel.saveSuccess.test {
            viewModel.saveTask()
            awaitItem()
        }
    }
}
