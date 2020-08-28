package br.com.sailboat.todozy.core.presentation.helper

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import br.com.sailboat.todozy.core.platform.PreferencesHelper

class FirstTimeConfigurationsHelper {

    fun checkAndPerformFirstTimeConfigurations(context: Context) {
        val preferencesHelper = PreferencesHelper(context)

        if (preferencesHelper.isFirstTimeExecutingApp()) {
            val editor = preferencesHelper.getSharedPreferences().edit()
            editor.putBoolean(PreferencesHelper.PRIMEIRA_EXECUCAO_APP, false)
            initNotificationSound(editor)
            initNotificationVibrate(editor)

            editor.apply()
        }
    }

    private fun initNotificationVibrate(editor: SharedPreferences.Editor) {
        editor.putBoolean(PreferencesHelper.ALLOW_VIBRATION, true)
    }

    private fun initNotificationSound(editor: SharedPreferences.Editor) {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        editor.putString(PreferencesHelper.CURRENT_SOUND, uri.toString())
    }

}