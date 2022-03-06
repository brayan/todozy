package br.com.sailboat.todozy.feature.settings.impl.presentation.navigator

import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.feature.settings.impl.presentation.startSettingsActivity
import br.com.sailboat.todozy.feature.settings.presentation.navigator.SettingsNavigator

class SettingsNavigatorImpl : SettingsNavigator {

    override fun navigateToSettings(fragment: Fragment) {
        fragment.startSettingsActivity()
    }

}