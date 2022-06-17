package br.com.sailboat.todozy.feature.settings.impl.presentation.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.settings.impl.presentation.startSettingsActivity

class SettingsNavigatorImpl : SettingsNavigator {

    override fun navigateToSettings(context: Context, launcher: ActivityResultLauncher<Intent>) {
        context.startSettingsActivity(launcher)
    }
}
