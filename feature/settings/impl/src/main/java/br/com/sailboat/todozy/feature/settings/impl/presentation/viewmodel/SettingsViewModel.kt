package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmVibrateSettingUseCase
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
) : BaseViewModel<SettingsViewState, SettingsViewAction>() {

    override fun dispatchViewAction(viewAction: SettingsViewAction) {
        when (viewAction) {
            is SettingsViewAction.OnStart -> onStart()
            is SettingsViewAction.OnClickMenuAlarmTone -> onClickMenuAlarmTone()
            is SettingsViewAction.OnClickAbout -> onClickAbout()
            is SettingsViewAction.OnClickVibrateAlarm -> onClickVibrateAlarm(viewAction)
            is SettingsViewAction.OnSelectAlarmTone -> onSelectAlarmTone(viewAction)
        }
    }

    private fun onStart() = viewModelScope.launch {
        viewState.alarmSound.value = getAlarmSoundSettingUseCase()
        viewState.vibrate.value = getAlarmVibrateSettingUseCase()
    }

    private fun onClickMenuAlarmTone() {
        val alarmSound = viewState.alarmSound.value
        viewState.action.value = SettingsViewState.Action.NavigateToAlarmToneSelector(alarmSound)
    }

    private fun onClickAbout() {
        viewState.action.value = SettingsViewState.Action.NavigateToAbout
    }

    private fun onClickVibrateAlarm(viewAction: SettingsViewAction.OnClickVibrateAlarm) {
        viewModelScope.launch {
            viewState.vibrate.value = viewAction.vibrate
            setAlarmVibrateSettingUseCase(viewAction.vibrate)
        }
    }

    private fun onSelectAlarmTone(viewAction: SettingsViewAction.OnSelectAlarmTone) {
        viewModelScope.launch {
            viewState.alarmSound.value = viewAction.uri
            setAlarmSoundSettingUseCase(viewAction.uri)
        }
    }
}
