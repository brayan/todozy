package br.com.sailboat.todozy.features.settings.presentation

import android.net.Uri
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPContract

interface SettingsContract {

    interface View : BaseMVPContract.View {
        fun setCurrentToneAlarm(tone: Uri)
        fun setCurrentToneAlarmNone()
        fun showAlarmChooser(alarmSound: Uri?)
        fun setVibrateAlarm(shouldVibrate: Boolean)
    }

    interface Presenter : BaseMVPContract.Presenter {
        fun onSelectAlarmTone(alarmTone: Uri)
        fun onClickAlarmTone()
        fun onClickVibrateAlarm(vibrate: Boolean)
    }

}