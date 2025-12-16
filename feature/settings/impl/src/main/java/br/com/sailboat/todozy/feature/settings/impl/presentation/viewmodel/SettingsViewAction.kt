package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import android.net.Uri

internal sealed class SettingsViewAction {
    object NavigateToAbout : SettingsViewAction()
    data class NavigateToAlarmToneSelector(val uri: Uri?) : SettingsViewAction()
    data class CreateDatabaseBackupDocument(val fileName: String) : SettingsViewAction()
    object OpenDatabaseBackupDocument : SettingsViewAction()
    object ShowDatabaseExportSuccess : SettingsViewAction()
    object ShowDatabaseImportSuccess : SettingsViewAction()
    object ShowDatabaseBackupError : SettingsViewAction()
}
