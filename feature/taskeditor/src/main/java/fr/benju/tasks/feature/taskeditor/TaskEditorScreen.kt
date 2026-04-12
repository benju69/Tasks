@file:OptIn(ExperimentalMaterial3Api::class)

package fr.benju.tasks.feature.taskeditor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.benju.tasks.domain.model.Priority
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TaskEditorScreen(
    modifier: Modifier = Modifier,
    onTaskSaved: () -> Unit,
    onDismiss: () -> Unit,
    taskId: Long? = null,
    viewModel: TaskEditorViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val currentOnTaskSaved by rememberUpdatedState(onTaskSaved)
    val titleFocusRequester = remember { FocusRequester() }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
        try {
            titleFocusRequester.requestFocus()
        } catch (e: IllegalStateException) {
            // Ignore focus request failures, e.g., if the view is not yet attached
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.saveSuccess.collect {
            currentOnTaskSaved()
        }
    }

    if (viewState.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = viewState.dueDate
        )
        DatePickerDialog(
            onDismissRequest = { viewModel.hideDatePicker() },
            confirmButton = {
                TextButton(onClick = { viewModel.updateDueDate(datePickerState.selectedDateMillis) }) {
                    Text(stringResource(R.string.task_editor_save))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDatePicker() }) {
                    Text(stringResource(R.string.task_editor_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (taskId != null) stringResource(R.string.task_editor_title_edit)
                        else stringResource(R.string.task_editor_title_new)
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.task_editor_cancel))
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.saveTask() },
                        enabled = !viewState.isSaving
                    ) {
                        Text(stringResource(R.string.task_editor_save))
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
                label = { Text(stringResource(R.string.task_editor_field_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                singleLine = true
            )

            OutlinedTextField(
                value = viewState.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text(stringResource(R.string.task_editor_field_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Text(stringResource(R.string.task_editor_field_priority), style = MaterialTheme.typography.titleMedium)

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
                                Priority.LOW -> stringResource(R.string.priority_low)
                                Priority.MEDIUM -> stringResource(R.string.priority_medium)
                                Priority.HIGH -> stringResource(R.string.priority_high)
                            }
                            Text(label)
                        }
                    )
                }
            }

            // Due date row
            Text(stringResource(R.string.task_editor_field_due_date), style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.showDatePicker() },
                    modifier = Modifier.weight(1f)
                ) {
                    val label = viewState.dueDate?.let { formatDueDate(it) }
                        ?: stringResource(R.string.task_editor_due_date_none)
                    Text(label)
                }
                if (viewState.dueDate != null) {
                    TextButton(onClick = { viewModel.updateDueDate(null) }) {
                        Text(stringResource(R.string.task_editor_due_date_clear))
                    }
                }
            }

            viewState.error?.let { errorRes ->
                Text(
                    text = stringResource(errorRes),
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

private fun formatDueDate(epochMs: Long): String {
    val localDate = Instant.ofEpochMilli(epochMs)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    return localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
}
