package fr.benju.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.benju.tasks.feature.settings.SettingsScreen
import fr.benju.tasks.feature.settings.SettingsViewModel
import fr.benju.tasks.feature.taskeditor.TaskEditorScreen
import fr.benju.tasks.feature.tasklist.TaskListScreen
import fr.benju.tasks.ui.theme.TaskManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsViewModel: SettingsViewModel by viewModels()
        setContent {
            val isDarkMode by settingsViewModel.darkModeFlow.collectAsState(initial = false)
            TaskManagerTheme(isDarkMode) {
                TaskManagerMainApp(settingsViewModel)
            }
        }
    }
}

@Composable
fun TaskManagerMainApp(settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "task_list"
    ) {
        composable("task_list") {
            TaskListScreen(
                onAddTaskClick = {
                    navController.navigate("task_editor")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onTaskClick = { taskId ->
                    navController.navigate("task_editor?taskId=$taskId")
                }
            )
        }

        composable(
            route = "task_editor?taskId={taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId")
                ?.takeIf { it != -1L }
            TaskEditorScreen(
                taskId = taskId,
                onTaskSaved = {
                    navController.popBackStack()
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
