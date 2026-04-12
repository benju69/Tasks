@file:OptIn(ExperimentalMaterial3Api::class)

package fr.benju.tasks.feature.taskeditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.ui.Res
import fr.benju.tasks.ui.priority_high
import fr.benju.tasks.ui.priority_low
import fr.benju.tasks.ui.priority_medium
import fr.benju.tasks.ui.task_editor_cancel
import fr.benju.tasks.ui.task_editor_field_description
import fr.benju.tasks.ui.task_editor_field_priority
import fr.benju.tasks.ui.task_editor_field_title
import fr.benju.tasks.ui.task_editor_save
import fr.benju.tasks.ui.task_editor_title_edit
import fr.benju.tasks.ui.task_editor_title_new
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskEditorScreen(
    modifier: Modifier = Modifier,
    onTaskSaved: () -> Unit,
    onDismiss: () -> Unit,
    taskId: Long? = null,
    viewModel: TaskEditorViewModel = koinViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val currentOnTaskSaved by rememberUpdatedState(onTaskSaved)
    val titleFocusRequester = remember { FocusRequester() }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        } else {
            try {
                titleFocusRequester.requestFocus()
            } catch (e: IllegalStateException) {
                // Ignore focus request failures
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.saveSuccess.collect {
            currentOnTaskSaved()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (taskId != null) stringResource(Res.string.task_editor_title_edit)
                        else stringResource(Res.string.task_editor_title_new)
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(Res.string.task_editor_cancel))
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.saveTask() },
                        enabled = !viewState.isSaving
                    ) {
                        Text(stringResource(Res.string.task_editor_save))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewState.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text(stringResource(Res.string.task_editor_field_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                singleLine = true
            )

            OutlinedTextField(
                value = viewState.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text(stringResource(Res.string.task_editor_field_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Text(stringResource(Res.string.task_editor_field_priority), style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Priority.entries.forEach { priority ->
                    FilterChip(
                        selected = viewState.priority == priority,
                        onClick = { viewModel.updatePriority(priority) },
                        label = {
                            val label = when (priority) {
                                Priority.LOW -> stringResource(Res.string.priority_low)
                                Priority.MEDIUM -> stringResource(Res.string.priority_medium)
                                Priority.HIGH -> stringResource(Res.string.priority_high)
                            }
                            Text(label)
                        }
                    )
                }
            }

            viewState.error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (viewState.isSaving) {
                CircularProgressIndicator()
            }
        }
    }
}
