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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.ui.Res
import fr.benju.tasks.ui.app_name
import fr.benju.tasks.ui.cd_add_task
import fr.benju.tasks.ui.cd_settings
import fr.benju.tasks.ui.task_list_empty
import fr.benju.tasks.ui.task_list_filter_active
import fr.benju.tasks.ui.task_list_filter_all
import fr.benju.tasks.ui.task_list_filter_completed
import fr.benju.tasks.ui.components.TaskCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = koinViewModel()
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
                title = { Text(stringResource(Res.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(Res.string.cd_settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(Res.string.cd_add_task)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = viewState.filter == TaskFilter.ALL,
                    onClick = { onFilterSelected(TaskFilter.ALL) },
                    label = { Text(stringResource(Res.string.task_list_filter_all)) }
                )
                FilterChip(
                    selected = viewState.filter == TaskFilter.ACTIVE,
                    onClick = { onFilterSelected(TaskFilter.ACTIVE) },
                    label = { Text(stringResource(Res.string.task_list_filter_active)) }
                )
                FilterChip(
                    selected = viewState.filter == TaskFilter.COMPLETED,
                    onClick = { onFilterSelected(TaskFilter.COMPLETED) },
                    label = { Text(stringResource(Res.string.task_list_filter_completed)) }
                )
            }

            if (viewState.tasks.isEmpty() && !viewState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.task_list_empty))
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
