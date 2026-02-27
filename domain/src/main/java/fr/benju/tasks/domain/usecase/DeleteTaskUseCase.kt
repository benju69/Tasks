package fr.benju.tasks.domain.usecase

interface DeleteTaskUseCase {
    suspend operator fun invoke(taskId: Long): Result<Unit>
}
