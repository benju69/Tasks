package fr.benju.tasks.data.repository

import fr.benju.tasks.data.database.dao.TaskDao
import fr.benju.tasks.data.database.entity.TaskEntity
import fr.benju.tasks.data.mapper.TaskMapper
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.Task
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class TaskRepositoryImplTest {

    private val taskDao: TaskDao = mockk()
    private val taskMapper = TaskMapper()
    private val repository = TaskRepositoryImpl(taskDao, taskMapper)

    @Test
    fun `addTask should insert task and return success with id`() = runTest {
        // Given
        val task = Task(
            id = 0,
            title = "New Task",
            description = "Description",
            priority = Priority.HIGH,
            isCompleted = false
        )
        coEvery { taskDao.insertTask(any()) } returns 1L

        // When
        val result = repository.addTask(task)

        // Then
        result.isSuccess shouldBeEqualTo true
        result.getOrNull() shouldBeEqualTo 1L
        coVerify { taskDao.insertTask(any()) }
    }

    @Test
    fun `updateTask should call dao update`() = runTest {
        // Given
        val task = Task(
            id = 1L,
            title = "Updated Task",
            description = "Updated Description",
            priority = Priority.LOW,
            isCompleted = true
        )
        coEvery { taskDao.updateTask(any()) } returns Unit

        // When
        val result = repository.updateTask(task)

        // Then
        result.isSuccess shouldBeEqualTo true
        coVerify { taskDao.updateTask(any()) }
    }

    @Test
    fun `deleteTask should call dao delete`() = runTest {
        // Given
        val taskId = 1L
        coEvery { taskDao.deleteTask(taskId) } returns Unit

        // When
        val result = repository.deleteTask(taskId)

        // Then
        result.isSuccess shouldBeEqualTo true
        coVerify { taskDao.deleteTask(taskId) }
    }
}
