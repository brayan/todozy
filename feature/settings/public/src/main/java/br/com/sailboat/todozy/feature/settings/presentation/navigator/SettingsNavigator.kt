package br.com.sailboat.todozy.feature.settings.presentation.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface SettingsNavigator {
    fun navigateToSettings(context: Context, launcher: ActivityResultLauncher<Intent>)
}