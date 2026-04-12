package fr.benju.tasks.domain.service

interface ReminderScheduler {
    fun schedule(taskId: Long, taskTitle: String, dueDate: Long)
    fun cancel(taskId: Long)
}
