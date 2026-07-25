package fr.benju.tasks.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import fr.benju.tasks.R
import fr.benju.tasks.domain.model.RepeatInterval
import fr.benju.tasks.domain.service.ReminderScheduler
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class TaskReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    @Inject
    lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @Inject
    lateinit var updateTaskUseCase: UpdateTaskUseCase

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        val taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE) ?: return
        if (taskId == -1L) return

        // ── Show notification ────────────────────────────────────────────────
        val hasPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        if (hasPermission) {
            val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_monochrome)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text, taskTitle))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(taskId.hashCode(), notification)
        }

        // ── Reschedule repeating tasks ────────────────────────────────────────
        val dueDateMs = intent.getLongExtra(EXTRA_DUE_DATE_MS, -1L)
        val repeatInterval = runCatching {
            RepeatInterval.valueOf(intent.getStringExtra(EXTRA_REPEAT_INTERVAL) ?: "NONE")
        }.getOrDefault(RepeatInterval.NONE)

        if (repeatInterval != RepeatInterval.NONE && dueDateMs != -1L) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val nextDueDate = computeNextOccurrence(dueDateMs, repeatInterval)
                    val task = getTaskByIdUseCase(taskId)
                    if (task != null) {
                        updateTaskUseCase(task.copy(dueDate = nextDueDate))
                        reminderScheduler.schedule(taskId, taskTitle, nextDueDate, repeatInterval)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_DUE_DATE_MS = "extra_due_date_ms"
        const val EXTRA_REPEAT_INTERVAL = "extra_repeat_interval"

        fun computeNextOccurrence(currentMs: Long, interval: RepeatInterval): Long {
            val zdt = Instant.ofEpochMilli(currentMs).atZone(ZoneId.systemDefault())
            return when (interval) {
                RepeatInterval.DAILY -> zdt.plusDays(1)
                RepeatInterval.WEEKLY -> zdt.plusWeeks(1)
                RepeatInterval.MONTHLY -> zdt.plusMonths(1)
                RepeatInterval.NONE -> zdt
            }.toInstant().toEpochMilli()
        }
    }
}
