package fr.benju.tasks.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getDarkMode(): Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
}

