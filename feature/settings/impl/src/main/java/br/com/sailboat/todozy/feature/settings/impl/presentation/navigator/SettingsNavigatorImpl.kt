package br.com.sailboat.todozy.feature.settings.impl.presentation.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.feature.settings.impl.presentation.startSettingsActivity
import br.com.sailboat.todozy.feature.settings.presentation.navigator.SettingsNavigator

class SettingsNavigatorImpl : SettingsNavigator {

    override fun navigateToSettings(context: Context, launcher: ActivityResultLauncher<Intent>) {
        context.startSettingsActivity(launcher)
    }

}