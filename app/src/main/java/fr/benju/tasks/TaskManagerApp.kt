package fr.benju.tasks

import android.app.Application
import fr.benju.tasks.data.database.DatabaseDriverFactory
import fr.benju.tasks.data.di.dataModule
import fr.benju.tasks.data.preferences.SettingsFactory
import fr.benju.tasks.feature.settings.di.settingsModule
import fr.benju.tasks.feature.taskeditor.di.taskEditorModule
import fr.benju.tasks.feature.tasklist.di.taskListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TaskManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TaskManagerApp)
            modules(
                dataModule,
                module {
                    single { DatabaseDriverFactory(androidContext()) }
                    single { SettingsFactory(androidContext()) }
                },
                taskListModule,
                taskEditorModule,
                settingsModule,
            )
        }
    }
}
