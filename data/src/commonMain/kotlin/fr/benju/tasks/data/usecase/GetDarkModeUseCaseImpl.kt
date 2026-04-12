package fr.benju.tasks.data.usecase

import fr.benju.tasks.domain.repository.UserPreferencesRepository
import fr.benju.tasks.domain.usecase.GetDarkModeUseCase
import kotlinx.coroutines.flow.Flow

class GetDarkModeUseCaseImpl(
    private val repository: UserPreferencesRepository
) : GetDarkModeUseCase {
    override fun invoke(): Flow<Boolean> = repository.getDarkMode()
}
