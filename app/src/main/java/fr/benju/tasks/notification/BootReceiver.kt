package fr.benju.tasks.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import fr.benju.tasks.domain.model.TaskFilter
import fr.benju.tasks.domain.service.ReminderScheduler
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getTasksUseCase: GetTasksUseCase

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                getTasksUseCase(TaskFilter.ACTIVE)
                    .first()
                    .filter { it.dueDate != null }
                    .forEach { task ->
                        reminderScheduler.schedule(task.id, task.title, task.dueDate!!)
                    }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
