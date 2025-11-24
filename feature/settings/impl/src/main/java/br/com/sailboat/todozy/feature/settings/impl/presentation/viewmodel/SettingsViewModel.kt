package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.feature.settings.android.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    override val viewState: SettingsViewState = SettingsViewState(),
    private val getAlarmSoundSettingUseCase: GetAlarmSoundSettingUseCase,
    private val setAlarmSoundSettingUseCase: SetAlarmSoundSettingUseCase,
    private val getAlarmVibrateSettingUseCase: GetAlarmVibrateSettingUseCase,
    private val setAlarmVibrateSettingUseCase: SetAlarmVibrateSettingUseCase,
) : BaseViewModel<SettingsViewState, SettingsViewIntent>() {
    override fun dispatchViewIntent(viewIntent: SettingsViewIntent) {
        when (viewIntent) {
            is SettingsViewIntent.OnStart -> onStart()
            is SettingsViewIntent.OnClickMenuAlarmTone -> onClickMenuAlarmTone()
            is SettingsViewIntent.OnClickAbout -> onClickAbout()
            is SettingsViewIntent.OnClickVibrateAlarm -> onClickVibrateAlarm(viewIntent)
            is SettingsViewIntent.OnSelectAlarmTone -> onSelectAlarmTone(viewIntent)
        }
    }

    private fun onStart() =
        viewModelScope.launch {
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
}
