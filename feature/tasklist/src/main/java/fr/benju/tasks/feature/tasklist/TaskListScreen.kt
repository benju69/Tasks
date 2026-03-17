package fr.benju.tasks.feature.tasklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.ui.components.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onSettingsClick : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Text("+")
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
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = viewState.filter == TaskFilter.ALL,
                    onClick = { viewModel.applyFilter(TaskFilter.ALL) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = viewState.filter == TaskFilter.ACTIVE,
                    onClick = { viewModel.applyFilter(TaskFilter.ACTIVE) },
                    label = { Text("Active") }
                )
                FilterChip(
                    selected = viewState.filter == TaskFilter.COMPLETED,
                    onClick = { viewModel.applyFilter(TaskFilter.COMPLETED) },
                    label = { Text("Completed") }
                )
            }

            // Task list
            if (viewState.tasks.isEmpty() && !viewState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks yet")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewState.tasks, key = { task -> task.id }) { task ->
                        TaskCard(
                            task = task,
                            onTaskClick = { id -> onTaskClick(id) },
                            onToggleComplete = { id -> viewModel.onTaskToggled(id) }
                        )
                    }
                }
            }
        }
    }
}
