package br.com.sailboat.todozy.feature.settings.data.datasource

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class SettingsLocalDataSourceImpl(context: Context) : SettingsLocalDataSource {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
    }

    override fun getAlarmTone(): Uri? {
        val currentSound = sharedPreferences.getString(CURRENT_SOUND, null)
        return currentSound?.run { Uri.parse(currentSound) }
    }

    override fun setAlarmTone(alarmTone: Uri) {
        getEditor().putString(CURRENT_SOUND, alarmTone.toString()).commit()
    }

    override fun getAlarmVibrate(): Boolean {
        return sharedPreferences.getBoolean(ALLOW_VIBRATION, true)
    }

    override fun setAlarmVibrate(vibrate: Boolean) {
        getEditor().putBoolean(ALLOW_VIBRATION, vibrate).commit()
    }

    override fun setFirstTimeLaunchingApp(firstTimeLaunching: Boolean) {
        getEditor().putBoolean(FIRST_TIME_LAUNCHING_APP, firstTimeLaunching).commit()
    }

    override fun isFirstTimeLaunchingApp(): Boolean {
        return sharedPreferences.getBoolean(FIRST_TIME_LAUNCHING_APP, true)
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    companion object {
        const val PREFERENCE_FILE_KEY = "br.com.sailboat.todozy.PREFERENCE_FILE_KEY"
        const val FIRST_TIME_LAUNCHING_APP = "FIRST_TIME_LAUNCHING_APP"
        const val CURRENT_SOUND = "CURRENT_SOUND"
        const val ALLOW_VIBRATION = "ALLOW_VIBRATION"
    }

}