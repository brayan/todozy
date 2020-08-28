package br.com.sailboat.todozy.core.platform

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class PreferencesHelper(private val context: Context) {

    companion object {
        val PREFERENCE_FILE_KEY = "com.brayanbedritchuk.todozy.android.PREFERENCE_FILE_KEY"
        val PRIMEIRA_EXECUCAO_APP = "PRIMEIRA_EXECUCAO_APP"
        val CURRENT_SOUND = "CURRENT_SOUND"
        val ALLOW_VIBRATION = "ALLOW_VIBRATION"
        val ADDED_TASK = "ADDED_TASK"
    }


    fun isFirstTimeExecutingApp(): Boolean {
        val sharedPref = getSharedPreferences()
        return sharedPref.getBoolean(PRIMEIRA_EXECUCAO_APP, true)
    }

    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
    }

    fun getCurrentNotificationSound(): Uri? {
        val sharedPref = getSharedPreferences()
        val currentSound = sharedPref.getString(CURRENT_SOUND, null)
        return if (currentSound != null) {
            Uri.parse(currentSound)
        } else null
    }


    fun setCurrentNotificationSound(uri: Uri?) {
        if (uri != null) {
            getEditor().putString(CURRENT_SOUND, uri.toString()).commit()
        } else {
            getEditor().putString(CURRENT_SOUND, null).commit()
        }

    }

    fun setCurrentVibrateSetting(isChecked: Boolean) {
        getEditor().putBoolean(ALLOW_VIBRATION, isChecked).commit()
    }

    fun isVibrationSettingAllowed(): Boolean {
        return getSharedPreferences().getBoolean(ALLOW_VIBRATION, true)
    }

    fun isSomeTaskAdded(): Boolean {
        val sharedPref = getSharedPreferences()
        return sharedPref.getBoolean(ADDED_TASK, false)
    }

    private fun getEditor(): SharedPreferences.Editor {
        return getSharedPreferences().edit()
    }

    fun setSomeTaskAdded(isAdded: Boolean) {
        getEditor().putBoolean(ADDED_TASK, isAdded).commit()
    }


}