package fr.benju.tasks.desktop

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import fr.benju.tasks.data.database.DatabaseDriverFactory
import fr.benju.tasks.data.di.dataModule
import fr.benju.tasks.data.preferences.SettingsFactory
import fr.benju.tasks.feature.settings.SettingsViewModel
import fr.benju.tasks.feature.settings.di.settingsModule
import fr.benju.tasks.feature.taskeditor.di.taskEditorModule
import fr.benju.tasks.feature.tasklist.di.taskListModule
import fr.benju.tasks.ui.theme.TaskManagerTheme
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.module

fun main() = application {
    KoinApplication(application = {
        modules(
            dataModule,
            module {
                single { DatabaseDriverFactory() }
                single { SettingsFactory() }
            },
            taskListModule,
            taskEditorModule,
            settingsModule,
        )
    }) {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Task Manager"
        ) {
            val settingsViewModel: SettingsViewModel = koinViewModel()
            val isDarkMode by settingsViewModel.darkModeFlow.collectAsState(initial = false)
            TaskManagerTheme(isDarkMode) {
                DesktopApp(settingsViewModel)
            }
        }
    }
}
