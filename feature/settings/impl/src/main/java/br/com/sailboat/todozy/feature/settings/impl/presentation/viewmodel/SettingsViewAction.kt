package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import android.net.Uri

sealed class SettingsViewAction {
    object OnStart : SettingsViewAction()
    object OnClickMenuAlarmTone : SettingsViewAction()
    object OnClickAbout : SettingsViewAction()
    data class OnClickVibrateAlarm(val vibrate: Boolean) : SettingsViewAction()
    data class OnSelectAlarmTone(val uri: Uri) : SettingsViewAction()
}