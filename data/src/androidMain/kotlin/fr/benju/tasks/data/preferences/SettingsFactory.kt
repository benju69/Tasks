package fr.benju.tasks.data.preferences

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings

actual class SettingsFactory(private val context: Context) {
    actual fun createSettings(): FlowSettings {
        return SharedPreferencesSettings(
            context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        ).toFlowSettings()
    }
}
