package fr.benju.tasks.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fr.benju.tasks.domain.model.Task
import fr.benju.tasks.ui.R

@Composable
fun TaskCard(
    task: Task,
    onTaskClick: (Long) -> Unit,
    onToggleComplete: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteTask: ((Long) -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete(task.id) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    maxLines = 1,
                )

                if (task.description != "") {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                PriorityChip(priority = task.priority)
            }

            if (onDeleteTask != null) {
                IconButton(onClick = { onDeleteTask(task.id) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_rounded_24dp),
                        contentDescription = "Delete task",
                        tint = Color.Red.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}
