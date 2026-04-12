package fr.benju.tasks.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetDarkModeUseCase {
    operator fun invoke(): Flow<Boolean>
}

