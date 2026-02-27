package fr.benju.tasks.domain.usecase

interface ToggleTaskStatusUseCase {
    suspend operator fun invoke(taskId: Long): Result<Unit>
}
