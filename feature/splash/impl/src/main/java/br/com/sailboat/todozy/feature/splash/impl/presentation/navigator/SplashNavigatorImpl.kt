package br.com.sailboat.todozy.feature.splash.impl.presentation.navigator

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.feature.navigation.android.SplashNavigator
import br.com.sailboat.todozy.feature.splash.impl.presentation.LauncherActivity

internal class SplashNavigatorImpl : SplashNavigator {

    override fun navigateToSplash(context: Context) {
        val intent = Intent(context, LauncherActivity::class.java)
        context.startActivity(intent)
    }
}
