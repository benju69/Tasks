package fr.benju.tasks.feature.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.ui.components.TaskCard
import fr.benju.tasks.ui.theme.TaskManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    TaskListContent(
        viewState = viewState,
        onAddTaskClick = onAddTaskClick,
        onTaskClick = onTaskClick,
        onSettingsClick = onSettingsClick,
        onFilterSelected = viewModel::applyFilter,
        onToggleComplete = viewModel::onTaskToggled,
        onDeleteTask = viewModel::onDeleteTask,
        onErrorShown = viewModel::clearError,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskListContent(
    viewState: TaskListViewState,
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onFilterSelected: (TaskFilter) -> Unit,
    onToggleComplete: (Long) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentOnErrorShown by rememberUpdatedState(onErrorShown)

    LaunchedEffect(viewState.error) {
        viewState.error?.let { message ->
            snackbarHostState.showSnackbar(message)
            currentOnErrorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        val icon = painterResource(R.drawable.ic_settings_rounded_24dp)
                        Icon(icon, contentDescription = stringResource(R.string.cd_settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                val icon = painterResource(R.drawable.ic_add_task_rounded_24dp)
                Icon(icon, contentDescription = stringResource(R.string.cd_add_task))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = viewState.filter == TaskFilter.ALL,
                    onClick = { onFilterSelected(TaskFilter.ALL) },
                    label = { Text(stringResource(R.string.task_list_filter_all)) }
                )
                FilterChip(
                    selected = viewState.filter == TaskFilter.ACTIVE,
                    onClick = { onFilterSelected(TaskFilter.ACTIVE) },
                    label = { Text(stringResource(R.string.task_list_filter_active)) }
                )
                FilterChip(
                    selected = viewState.filter == TaskFilter.COMPLETED,
                    onClick = { onFilterSelected(TaskFilter.COMPLETED) },
                    label = { Text(stringResource(R.string.task_list_filter_completed)) }
                )
            }

            // Task list
            if (viewState.tasks.isEmpty() && !viewState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.task_list_empty))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewState.tasks, key = { task -> task.id }) { task ->
                        TaskCard(
                            task = task,
                            onTaskClick = { id -> onTaskClick(id) },
                            onToggleComplete = { id -> onToggleComplete(id) },
                            onDeleteTask = { id -> onDeleteTask(id) }
                        )
                    }
                }
            }
        }
    }
}

// ── Preview data ──────────────────────────────────────────────────────────────

private val previewTasks = listOf(
    Task(id = 1, title = "Grocery shopping", description = "Buy fruits, vegetables and milk", priority = Priority.HIGH, isCompleted = true),
    Task(id = 2, title = "Call mom", description = "Catch up and schedule next family dinner", priority = Priority.MEDIUM),
    Task(id = 3, title = "Finish the project", description = "Complete the final report and send it to the team", priority = Priority.HIGH),
    Task(id = 4, title = "Read a book", description = "Continue reading \"Atomic Habits\" — chapter 5", priority = Priority.LOW),
    Task(id = 5, title = "Clean the house", description = "Vacuum the living room and clean the bathroom", priority = Priority.MEDIUM),
)

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Task List – Light", showBackground = true, showSystemUi = true)
@Composable
private fun TaskListContentLightPreview() {
    TaskManagerTheme(darkTheme = false) {
        TaskListContent(
            viewState = TaskListViewState(tasks = previewTasks, isLoading = false),
            onAddTaskClick = {},
            onTaskClick = {},
            onSettingsClick = {},
            onFilterSelected = {},
            onToggleComplete = {},
            onDeleteTask = {},
            onErrorShown = {},
        )
    }
}

@Preview(name = "Task List – Dark", showBackground = true, showSystemUi = true)
@Composable
private fun TaskListContentDarkPreview() {
    TaskManagerTheme(darkTheme = true) {
        Surface {
            TaskListContent(
                viewState = TaskListViewState(tasks = previewTasks, isLoading = false),
                onAddTaskClick = {},
                onTaskClick = {},
                onSettingsClick = {},
                onFilterSelected = {},
                onToggleComplete = {},
                onDeleteTask = {},
                onErrorShown = {},
            )
        }
    }
}

@Preview(name = "Task List – Empty", showBackground = true, showSystemUi = true)
@Composable
private fun TaskListContentEmptyPreview() {
    TaskManagerTheme(darkTheme = false) {
        TaskListContent(
            viewState = TaskListViewState(tasks = emptyList(), isLoading = false),
            onAddTaskClick = {},
            onTaskClick = {},
            onSettingsClick = {},
            onFilterSelected = {},
            onToggleComplete = {},
            onDeleteTask = {},
            onErrorShown = {},
        )
    }
}

