package br.com.sailboat.todozy.core.platform

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class PreferencesHelper(private val context: Context) {

    companion object {
        val PREFERENCE_FILE_KEY = "com.brayanbedritchuk.todozy.android.PREFERENCE_FILE_KEY"
        val PRIMEIRA_EXECUCAO_APP = "PRIMEIRA_EXECUCAO_APP"
        val ADDED_TASK = "ADDED_TASK"
    }


    fun isFirstTimeExecutingApp(): Boolean {
        val sharedPref = getSharedPreferences()
        return sharedPref.getBoolean(PRIMEIRA_EXECUCAO_APP, true)
    }

    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
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