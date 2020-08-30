package br.com.sailboat.todozy.features.settings.presentation

import android.net.Uri
import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmVibrateSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.SetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.SetAlarmVibrateSetting

class SettingsPresenter(private val getAlarmSoundSetting: GetAlarmSoundSetting,
                        private val setAlarmSoundSetting: SetAlarmSoundSetting,
                        private val getAlarmVibrateSetting: GetAlarmVibrateSetting,
                        private val setAlarmVibrateSetting: SetAlarmVibrateSetting) :
        BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {

    private var alarmSound: Uri? = null
    private var shouldVibrate = false

    override fun onStart() {
        launchAsync {
            alarmSound = getAlarmSoundSetting()
            alarmSound?.run { view?.setCurrentToneAlarm(this) } ?: view?.setCurrentToneAlarmNone()

            shouldVibrate = getAlarmVibrateSetting()
            view?.setVibrateAlarm(shouldVibrate)
        }
    }

    override fun onSelectAlarmTone(alarmTone: Uri) {
        launchAsync {
            setAlarmSoundSetting(alarmTone)
            view?.setCurrentToneAlarm(alarmTone)
        }
    }

    override fun onClickAlarmTone() {
        view?.showAlarmChooser(alarmSound)
    }

    override fun onClickVibrateAlarm(vibrate: Boolean) {
        launchAsync {
            shouldVibrate = vibrate
            setAlarmVibrateSetting(shouldVibrate)
            view?.setVibrateAlarm(shouldVibrate)
        }
    }

}