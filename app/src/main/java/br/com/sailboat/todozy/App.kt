package br.com.sailboat.todozy

import android.app.Application
import br.com.sailboat.todozy.core.platform.CrashlyticsReportingTree
import br.com.sailboat.todozy.di.appComponent
import br.com.sailboat.todozy.feature.about.di.aboutComponent
import br.com.sailboat.todozy.feature.alarm.di.alarmComponent
import br.com.sailboat.todozy.feature.settings.di.settingsComponent
import br.com.sailboat.todozy.feature.task.form.di.taskFormComponent
import br.com.sailboat.todozy.feature.task.history.di.taskHistoryComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(aboutComponent)
            modules(settingsComponent)
            modules(alarmComponent)
            modules(taskHistoryComponent)
            modules(taskFormComponent)
            modules(appComponent)
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