package br.com.sailboat.todozy.feature.navigation.android

import android.content.Context
import android.content.Intent

interface SplashNavigator {
    fun navigateToSplash(context: Context)
    fun getSplashIntent(context: Context): Intent
}
