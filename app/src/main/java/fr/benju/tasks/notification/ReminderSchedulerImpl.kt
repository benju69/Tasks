package fr.benju.tasks.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.benju.tasks.domain.service.ReminderScheduler
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderScheduler {

    override fun schedule(taskId: Long, taskTitle: String, dueDate: Long) {
        val triggerAt = computeTriggerTime(dueDate)
        if (triggerAt <= System.currentTimeMillis()) return

        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra(TaskReminderReceiver.EXTRA_TASK_ID, taskId)
            putExtra(TaskReminderReceiver.EXTRA_TASK_TITLE, taskTitle)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }

    override fun cancel(taskId: Long) {
        val intent = Intent(context, TaskReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) ?: return

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    /**
     * Computes the alarm trigger time as 9:00 AM local time on the due date.
     */
    private fun computeTriggerTime(dueDateMs: Long): Long {
        val localDate = Instant.ofEpochMilli(dueDateMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return localDate
            .atTime(LocalTime.of(9, 0))
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}
