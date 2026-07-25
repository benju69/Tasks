package fr.benju.tasks

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.benju.tasks.notification.NotificationHelper
import javax.inject.Inject

@HiltAndroidApp
class TaskManagerApp : Application() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel()
    }
}
