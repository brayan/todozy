package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.feature.settings.android.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseJsonBackupService
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class SettingsViewModel(
    override val viewState: SettingsViewState = SettingsViewState(),
    private val getAlarmSoundSettingUseCase: GetAlarmSoundSettingUseCase,
    private val setAlarmSoundSettingUseCase: SetAlarmSoundSettingUseCase,
    private val getAlarmVibrateSettingUseCase: GetAlarmVibrateSettingUseCase,
    private val setAlarmVibrateSettingUseCase: SetAlarmVibrateSettingUseCase,
    private val databaseJsonBackupService: DatabaseJsonBackupService,
    private val logService: LogService,
) : BaseViewModel<SettingsViewState, SettingsViewIntent>() {
    override fun dispatchViewIntent(viewIntent: SettingsViewIntent) {
        when (viewIntent) {
            is SettingsViewIntent.OnStart -> onStart()
            is SettingsViewIntent.OnClickMenuAlarmTone -> onClickMenuAlarmTone()
            is SettingsViewIntent.OnClickAbout -> onClickAbout()
            is SettingsViewIntent.OnClickExportDatabase -> onClickExportDatabase()
            is SettingsViewIntent.OnClickImportDatabase -> onClickImportDatabase()
            is SettingsViewIntent.OnClickVibrateAlarm -> onClickVibrateAlarm(viewIntent)
            is SettingsViewIntent.OnSelectAlarmTone -> onSelectAlarmTone(viewIntent)
            is SettingsViewIntent.OnSelectExportDatabaseUri -> onSelectExportDatabaseUri(viewIntent)
            is SettingsViewIntent.OnConfirmImportDatabase -> onConfirmImportDatabase(viewIntent)
        }
    }

    private fun onStart() = viewModelScope.launch {
        viewState.alarmSound.value = getAlarmSoundSettingUseCase()
        viewState.vibrate.value = getAlarmVibrateSettingUseCase()
    }

    private fun onClickMenuAlarmTone() {
        val alarmSound = viewState.alarmSound.value
        viewState.viewAction.value = SettingsViewAction.NavigateToAlarmToneSelector(alarmSound)
    }

    private fun onClickAbout() {
        viewState.viewAction.value = SettingsViewAction.NavigateToAbout
    }

    private fun onClickExportDatabase() {
        val timestamp =
            LocalDateTime
                .now(ZoneId.systemDefault())
                .format(BACKUP_FILE_NAME_FORMATTER)
        viewState.viewAction.value =
            SettingsViewAction.CreateDatabaseBackupDocument(
                "todozy-backup_$timestamp.json",
            )
    }

    private fun onClickImportDatabase() {
        viewState.viewAction.value = SettingsViewAction.OpenDatabaseBackupDocument
    }

    private fun onClickVibrateAlarm(viewIntent: SettingsViewIntent.OnClickVibrateAlarm) {
        viewModelScope.launch {
            viewState.vibrate.value = viewIntent.vibrate
            setAlarmVibrateSettingUseCase(viewIntent.vibrate)
        }
    }

    private fun onSelectAlarmTone(viewIntent: SettingsViewIntent.OnSelectAlarmTone) {
        viewModelScope.launch {
            viewState.alarmSound.value = viewIntent.uri
            setAlarmSoundSettingUseCase(viewIntent.uri)
        }
    }

    private fun onSelectExportDatabaseUri(viewIntent: SettingsViewIntent.OnSelectExportDatabaseUri) {
        viewModelScope.launch {
            try {
                databaseJsonBackupService.exportToJson(viewIntent.uri)
                viewState.viewAction.value = SettingsViewAction.ShowDatabaseExportSuccess
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = SettingsViewAction.ShowDatabaseBackupError
            }
        }
    }

    private fun onConfirmImportDatabase(viewIntent: SettingsViewIntent.OnConfirmImportDatabase) {
        viewModelScope.launch {
            try {
                databaseJsonBackupService.importFromJson(viewIntent.uri)
                viewState.viewAction.value = SettingsViewAction.ShowDatabaseImportSuccess
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = SettingsViewAction.ShowDatabaseBackupError
            }
        }
    }

    private companion object {
        private val BACKUP_FILE_NAME_FORMATTER =
            DateTimeFormatter.ofPattern(
                "yyyyMMdd_HHmmss",
                Locale.US,
            )
    }
}
