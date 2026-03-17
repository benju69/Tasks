package fr.benju.tasks.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.benju.tasks.domain.usecase.GetDarkModeUseCase
import fr.benju.tasks.domain.usecase.SetDarkModeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val setDarkModeUseCase: SetDarkModeUseCase
) : ViewModel() {

    val darkModeFlow: StateFlow<Boolean> = getDarkModeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun toggleDarkMode() {
        viewModelScope.launch {
            setDarkModeUseCase(!darkModeFlow.value)
        }
    }

    fun isDarkModeEnabled(): Boolean = darkModeFlow.value
}
