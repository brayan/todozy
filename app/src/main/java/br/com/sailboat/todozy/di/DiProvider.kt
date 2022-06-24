package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.feature.about.impl.di.aboutModule
import br.com.sailboat.todozy.feature.alarm.impl.di.alarmModule
import br.com.sailboat.todozy.feature.settings.impl.di.settingsModule
import br.com.sailboat.todozy.feature.task.details.impl.di.taskDetailsModule
import br.com.sailboat.todozy.feature.task.form.impl.di.taskFormModule
import br.com.sailboat.todozy.feature.task.history.impl.di.taskHistoryModule
import br.com.sailboat.todozy.feature.task.list.impl.di.taskListModule
import org.koin.core.module.Module

internal object DiProvider {

    fun getModules(): List<Module> = listOf(
        appModule,
        aboutModule,
        settingsModule,
        alarmModule,
        taskHistoryModule,
        taskFormModule,
        taskListModule,
        taskDetailsModule,
    ).flatten()
}
