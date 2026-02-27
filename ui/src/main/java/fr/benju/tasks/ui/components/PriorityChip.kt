package fr.benju.tasks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.benju.tasks.domain.model.Priority
import fr.benju.tasks.ui.theme.PriorityHigh
import fr.benju.tasks.ui.theme.PriorityLow
import fr.benju.tasks.ui.theme.PriorityMedium

@Composable
fun PriorityChip(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (priority) {
        Priority.LOW -> PriorityLow to "Low"
        Priority.MEDIUM -> PriorityMedium to "Medium"
        Priority.HIGH -> PriorityHigh to "High"
    }

    Text(
        text = label,
        modifier = modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelMedium,
        color = color
    )
}
