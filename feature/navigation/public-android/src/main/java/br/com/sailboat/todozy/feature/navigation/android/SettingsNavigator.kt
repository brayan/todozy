package br.com.sailboat.todozy.feature.navigation.android

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface SettingsNavigator {
    fun navigateToSettings(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
    )
}
