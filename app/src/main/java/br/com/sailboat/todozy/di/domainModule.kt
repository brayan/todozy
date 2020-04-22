package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.domain.alarm.*
import br.com.sailboat.todozy.domain.history.AddHistory
import br.com.sailboat.todozy.domain.tasks.*
import br.com.sailboat.todozy.ui.task.details.GetTaskDetailsView
import org.koin.dsl.module

val domainModule = module {
    factory { GetAlarm(get()) }
    factory { GetTask(get()) }
    factory { GetTasks(get()) }
    factory { GetTaskHistory(get()) }
    factory { GetTaskDetailsView(get(), get()) }
    factory { GetTaskMetrics(get()) }
    factory { DisableTask(get(), get()) }
    factory { MarkTask(get(), get(), get(), get(), get(), get()) }
    factory { DeleteAlarm(get()) }
    factory { GetNextAlarm() }
    factory { UpdateOldAlarm(get()) }
    factory { SetNextValidAlarm(get()) }
    factory { AddHistory(get()) }
    factory { SaveTask(get()) }
}