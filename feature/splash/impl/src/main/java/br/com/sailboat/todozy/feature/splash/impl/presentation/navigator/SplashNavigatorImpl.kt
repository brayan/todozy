package br.com.sailboat.todozy.feature.splash.impl.presentation.navigator

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.feature.navigation.android.SplashNavigator
import br.com.sailboat.todozy.feature.splash.impl.presentation.LauncherActivity

internal class SplashNavigatorImpl : SplashNavigator {

    override fun navigateToSplash(context: Context) {
        val intent = getSplashIntent(context)
        context.startActivity(intent)
    }

    override fun getSplashIntent(context: Context): Intent {
        return Intent(context, LauncherActivity::class.java)
    }
}
