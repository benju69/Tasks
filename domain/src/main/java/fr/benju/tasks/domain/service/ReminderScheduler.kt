package fr.benju.tasks.domain.service

import fr.benju.tasks.domain.model.RepeatInterval

interface ReminderScheduler {
    fun schedule(taskId: Long, taskTitle: String, dueDate: Long, repeatInterval: RepeatInterval)
    fun cancel(taskId: Long)
}
