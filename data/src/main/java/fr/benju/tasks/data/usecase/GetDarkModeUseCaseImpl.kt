package fr.benju.tasks.data.usecase

import fr.benju.tasks.domain.repository.UserPreferencesRepository
import fr.benju.tasks.domain.usecase.GetDarkModeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkModeUseCaseImpl @Inject constructor(
    private val repository: UserPreferencesRepository
) : GetDarkModeUseCase {

    override fun invoke(): Flow<Boolean> = repository.getDarkMode()
}

