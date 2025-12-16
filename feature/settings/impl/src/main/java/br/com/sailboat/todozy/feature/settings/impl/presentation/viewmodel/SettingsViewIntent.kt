package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import android.net.Uri

internal sealed class SettingsViewIntent {
    object OnStart : SettingsViewIntent()
    object OnClickMenuAlarmTone : SettingsViewIntent()
    object OnClickAbout : SettingsViewIntent()
    object OnClickExportDatabase : SettingsViewIntent()
    object OnClickImportDatabase : SettingsViewIntent()
    data class OnClickVibrateAlarm(val vibrate: Boolean) : SettingsViewIntent()
    data class OnSelectAlarmTone(val uri: Uri) : SettingsViewIntent()
    data class OnSelectExportDatabaseUri(val uri: Uri) : SettingsViewIntent()
    data class OnConfirmImportDatabase(val uri: Uri) : SettingsViewIntent()
}
