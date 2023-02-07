package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import android.net.Uri

internal sealed class SettingsViewIntent {
    object OnStart : SettingsViewIntent()
    object OnClickMenuAlarmTone : SettingsViewIntent()
    object OnClickAbout : SettingsViewIntent()
    data class OnClickVibrateAlarm(val vibrate: Boolean) : SettingsViewIntent()
    data class OnSelectAlarmTone(val uri: Uri) : SettingsViewIntent()
}
