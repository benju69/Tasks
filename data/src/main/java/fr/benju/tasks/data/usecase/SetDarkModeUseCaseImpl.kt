package fr.benju.tasks.data.usecase

import fr.benju.tasks.domain.repository.UserPreferencesRepository
import fr.benju.tasks.domain.usecase.SetDarkModeUseCase
import javax.inject.Inject

class SetDarkModeUseCaseImpl @Inject constructor(
    private val repository: UserPreferencesRepository
) : SetDarkModeUseCase {
    override suspend fun invoke(enabled: Boolean) = repository.setDarkMode(enabled)
}
