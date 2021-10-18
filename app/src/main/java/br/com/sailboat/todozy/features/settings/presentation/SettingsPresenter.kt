package br.com.sailboat.todozy.features.settings.presentation

import android.net.Uri
import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter
import br.com.sailboat.todozy.features.settings.domain.usecase.*

class SettingsPresenter(
    private val getAlarmSoundSettingUseCase: GetAlarmSoundSettingUseCase,
    private val setAlarmSoundSettingUseCase: SetAlarmSoundSettingUseCase,
    private val getAlarmVibrateSettingUseCase: GetAlarmVibrateSettingUseCase,
    private val setAlarmVibrateSetting: SetAlarmVibrateSetting
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
            setAlarmVibrateSetting(shouldVibrate)
            view?.setVibrateAlarm(shouldVibrate)
        }
    }

}