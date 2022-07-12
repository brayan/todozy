package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.feature.about.impl.di.aboutModule
import br.com.sailboat.todozy.feature.alarm.impl.di.alarmModule
import br.com.sailboat.todozy.feature.settings.impl.di.settingsModule
import br.com.sailboat.todozy.feature.splash.impl.di.splashModule
import br.com.sailboat.todozy.feature.task.details.impl.di.taskDetailsModule
import br.com.sailboat.todozy.feature.task.form.impl.di.taskFormModule
import br.com.sailboat.todozy.feature.task.history.impl.di.taskHistoryModule
import br.com.sailboat.todozy.feature.task.list.impl.di.taskListModule
import br.com.sailboat.todozy.platform.impl.di.platformModule
import br.com.sailboat.uicomponent.impl.di.uiComponentModule
import org.koin.core.module.Module

internal object DiProvider {

    val modules: List<Module>
        get() = listOf(
            platformModule,
            aboutModule,
            settingsModule,
            alarmModule,
            splashModule,
            taskHistoryModule,
            taskFormModule,
            taskListModule,
            taskDetailsModule,
            uiComponentModule,
        ).flatten()
}
