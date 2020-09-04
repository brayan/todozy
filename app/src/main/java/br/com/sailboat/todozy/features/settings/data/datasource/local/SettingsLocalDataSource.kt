package br.com.sailboat.todozy.features.settings.data.datasource.local

import android.net.Uri

interface SettingsLocalDataSource {
    fun getAlarmTone(): Uri?
    fun setAlarmTone(alarmTone: Uri)
    fun getAlarmVibrate(): Boolean
    fun setAlarmVibrate(vibrate: Boolean)
    fun setFirstTimeLaunchingApp(firstTimeLaunching: Boolean)
    fun isFirstTimeLaunchingApp(): Boolean
}