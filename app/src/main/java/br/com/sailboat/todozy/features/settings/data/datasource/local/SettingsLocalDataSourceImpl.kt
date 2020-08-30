package br.com.sailboat.todozy.features.settings.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class SettingsLocalDataSourceImpl(context: Context) : SettingsLocalDataSource {

    companion object {
        val PREFERENCE_FILE_KEY = "br.com.sailboat.todozy.PREFERENCE_FILE_KEY"
        val PRIMEIRA_EXECUCAO_APP = "PRIMEIRA_EXECUCAO_APP"
        val CURRENT_SOUND = "CURRENT_SOUND"
        val ALLOW_VIBRATION = "ALLOW_VIBRATION"
        val ADDED_TASK = "ADDED_TASK"
    }

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

    fun isFirstTimeExecutingApp(): Boolean {
        return sharedPreferences.getBoolean(PRIMEIRA_EXECUCAO_APP, true)
    }

    fun isSomeTaskAdded(): Boolean {
        return sharedPreferences.getBoolean(ADDED_TASK, false)
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    fun setSomeTaskAdded(isAdded: Boolean) {
        getEditor().putBoolean(ADDED_TASK, isAdded).commit()
    }

}