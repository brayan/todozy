package br.com.sailboat.todozy.feature.settings.domain.repository

import android.net.Uri

interface SettingsRepository {
    suspend fun getAlarmTone(): Uri?
    suspend fun getAlarmVibrate(): Boolean
    suspend fun setAlarmTone(toneUri: Uri)
    suspend fun setAlarmVibrate(vibrate: Boolean)
    suspend fun isFirstTimeLaunchingApp(): Boolean
    suspend fun setFirstTimeLaunchingApp(firstTime: Boolean)
    suspend fun setDefaultAlarmTone()
}