package br.com.sailboat.todozy

import android.app.Application
import br.com.sailboat.todozy.di.DiProvider
import br.com.sailboat.todozy.platform.impl.CrashlyticsReportingTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree

internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(DiProvider.modules)
        }
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashlyticsReportingTree())
        }
    }
}
