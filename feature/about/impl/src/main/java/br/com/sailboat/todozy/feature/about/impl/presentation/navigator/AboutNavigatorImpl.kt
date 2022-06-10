package br.com.sailboat.todozy.feature.about.impl.presentation.navigator

import android.content.Context
import br.com.sailboat.todozy.feature.about.impl.presentation.startAboutActivity
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator

class AboutNavigatorImpl : AboutNavigator {

    override fun navigateToAbout(context: Context) {
        context.startAboutActivity()
    }
}
