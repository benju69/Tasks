package fr.benju.tasks.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _isDarkModeEnabled = MutableStateFlow(false)
    val darkModeFlow: StateFlow<Boolean> = _isDarkModeEnabled.asStateFlow()

    fun toggleDarkMode() {
        _isDarkModeEnabled.value = !_isDarkModeEnabled.value
    }

    fun isDarkModeEnabled(): Boolean {
        return _isDarkModeEnabled.value
    }
}
