package fr.benju.tasks.feature.settings

import app.cash.turbine.test
import fr.benju.tasks.core.test.CoroutineTestExtension
import fr.benju.tasks.domain.usecase.GetDarkModeUseCase
import fr.benju.tasks.domain.usecase.SetDarkModeUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class SettingsViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutineExtension = CoroutineTestExtension()

    private val darkModeState = MutableStateFlow(false)
    private val getDarkModeUseCase: GetDarkModeUseCase = mockk()
    private val setDarkModeUseCase: SetDarkModeUseCase = mockk()

    @BeforeEach
    fun setUp() {
        every { getDarkModeUseCase() } returns darkModeState
        coEvery { setDarkModeUseCase(any()) } coAnswers { darkModeState.value = firstArg<Boolean>() }
    }

    @Test
    fun `initial dark mode should be false`() = runTest {
        // Given
        val viewModel = SettingsViewModel(getDarkModeUseCase, setDarkModeUseCase)

        // When
        val isDarkMode = viewModel.darkModeFlow.first()

        // Then
        isDarkMode shouldBeEqualTo false
    }

    @Test
    fun `toggleDarkMode should update dark mode value`() = runTest {
        // Given
        val viewModel = SettingsViewModel(getDarkModeUseCase, setDarkModeUseCase)

        // When / Then
        viewModel.darkModeFlow.test {
            awaitItem() // initial false
            viewModel.toggleDarkMode()
            awaitItem() shouldBeEqualTo true
        }
    }
}
