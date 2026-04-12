package fr.benju.tasks.data.preferences

import com.russhwolf.settings.coroutines.FlowSettings

expect class SettingsFactory {
    fun createSettings(): FlowSettings
}
