package br.com.sailboat.todozy.feature.settings.impl.presentation

import android.net.Uri
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.utility.android.mvp.BasePresenter

class SettingsPresenter(
    private val getAlarmSoundSettingUseCase: GetAlarmSoundSettingUseCase,
    private val setAlarmSoundSettingUseCase: SetAlarmSoundSettingUseCase,
    private val getAlarmVibrateSettingUseCase: GetAlarmVibrateSettingUseCase,
    private val setAlarmVibrateSettingUseCase: SetAlarmVibrateSettingUseCase
) : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {

    private var alarmSound: Uri? = null
    private var shouldVibrate = false

    override fun onStart() {
        launchMain {
            alarmSound = getAlarmSoundSettingUseCase()
            alarmSound?.run { view?.setCurrentToneAlarm(this) } ?: view?.setCurrentToneAlarmNone()

            shouldVibrate = getAlarmVibrateSettingUseCase()
            view?.setVibrateAlarm(shouldVibrate)
        }
    }

    override fun onSelectAlarmTone(alarmTone: Uri) {
        launchMain {
            setAlarmSoundSettingUseCase(alarmTone)
            view?.setCurrentToneAlarm(alarmTone)
        }
    }

    override fun onClickAlarmTone() {
        view?.showAlarmChooser(alarmSound)
    }

    override fun onClickVibrateAlarm(vibrate: Boolean) {
        launchMain {
            shouldVibrate = vibrate
            setAlarmVibrateSettingUseCase(shouldVibrate)
            view?.setVibrateAlarm(shouldVibrate)
        }
    }

}