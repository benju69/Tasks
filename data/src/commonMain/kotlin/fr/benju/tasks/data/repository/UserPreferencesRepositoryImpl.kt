package fr.benju.tasks.data.repository

import com.russhwolf.settings.coroutines.FlowSettings
import fr.benju.tasks.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    private val settings: FlowSettings,
) : UserPreferencesRepository {

    companion object {
        private const val DARK_MODE_KEY = "dark_mode"
    }

    override fun getDarkMode(): Flow<Boolean> {
        return settings.getBooleanFlow(DARK_MODE_KEY, false)
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        settings.putBoolean(DARK_MODE_KEY, enabled)
    }
}
