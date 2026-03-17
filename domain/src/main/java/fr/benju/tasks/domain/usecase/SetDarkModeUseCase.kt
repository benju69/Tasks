package fr.benju.tasks.domain.usecase

interface SetDarkModeUseCase {
    suspend operator fun invoke(enabled: Boolean)
}
