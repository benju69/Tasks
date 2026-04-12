package fr.benju.tasks.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.benju.tasks.domain.service.ReminderScheduler
import fr.benju.tasks.notification.ReminderSchedulerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindReminderScheduler(
        impl: ReminderSchedulerImpl
    ): ReminderScheduler
}
