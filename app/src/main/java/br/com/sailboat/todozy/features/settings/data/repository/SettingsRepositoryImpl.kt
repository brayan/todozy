package br.com.sailboat.todozy.features.settings.data.repository

import android.net.Uri
import br.com.sailboat.todozy.features.settings.data.datasource.local.SettingsLocalDataSource
import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

class SettingsRepositoryImpl(private val localDataSource: SettingsLocalDataSource) : SettingsRepository {

    override suspend fun getAlarmTone() = localDataSource.getAlarmTone()
    override suspend fun getAlarmVibrate() = localDataSource.getAlarmVibrate()
    override suspend fun setAlarmTone(toneUri: Uri) = localDataSource.setAlarmTone(toneUri)
    override suspend fun setAlarmVibrate(vibrate: Boolean) = localDataSource.setAlarmVibrate(vibrate)

}