package fr.benju.tasks.feature.settings

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class SettingsViewModelTest {

    @Test
    fun `initial dark mode should be false`() = runTest {
        // Given
        val viewModel = SettingsViewModel()

        // When
        val isDarkMode = viewModel.darkModeFlow.first()

        // Then
        isDarkMode shouldBeEqualTo false
    }

    @Test
    fun `toggleDarkMode should update dark mode value`() = runTest {
        // Given
        val viewModel = SettingsViewModel()

        // When
        viewModel.toggleDarkMode()

        // Then
        viewModel.isDarkModeEnabled() shouldBeEqualTo true
    }
}
