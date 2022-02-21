package br.com.sailboat.todozy.features.settings.data.repository

import android.media.RingtoneManager
import android.net.Uri
import br.com.sailboat.todozy.features.settings.data.datasource.local.SettingsLocalDataSource
import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

class SettingsRepositoryImpl(private val localDataSource: SettingsLocalDataSource) :
    SettingsRepository {

    override suspend fun setAlarmTone(toneUri: Uri) = localDataSource.setAlarmTone(toneUri)
    override suspend fun getAlarmTone() = localDataSource.getAlarmTone()
    override suspend fun setAlarmVibrate(vibrate: Boolean) =
        localDataSource.setAlarmVibrate(vibrate)

    override suspend fun getAlarmVibrate() = localDataSource.getAlarmVibrate()
    override suspend fun setFirstTimeLaunchingApp(firstTime: Boolean) =
        localDataSource.setFirstTimeLaunchingApp(firstTime)

    override suspend fun isFirstTimeLaunchingApp() = localDataSource.isFirstTimeLaunchingApp()
    override suspend fun setDefaultAlarmTone() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        setAlarmTone(uri)
    }

}