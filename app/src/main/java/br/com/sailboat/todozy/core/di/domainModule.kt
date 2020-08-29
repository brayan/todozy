package br.com.sailboat.todozy.core.di

import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.AddHistory
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.DeleteAllHistory
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.DeleteHistory
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.UpdateHistory
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.*
import br.com.sailboat.todozy.features.tasks.presentation.details.GetTaskDetailsView
import org.koin.dsl.module

val domainModule = module {
    factory { GetAlarm(get()) }
    factory { GetTask(get()) }
    factory { GetTasks(get()) }
    factory { GetTaskHistory(get()) }
    factory { GetTaskDetailsView(get(), get()) }
    factory { GetTaskMetrics(get()) }
    factory { DisableTask(get(), get()) }
    factory { CompleteTask(get(), get(), get(), get(), get()) }
    factory { DeleteAlarm(get()) }
    factory { GetNextAlarm() }
    factory { UpdateOldAlarm(get()) }
    factory { SetNextValidAlarm(get()) }
    factory { AddHistory(get()) }
    factory { UpdateHistory(get()) }
    factory { DeleteHistory(get()) }
    factory { DeleteAllHistory(get()) }
    factory { SaveTask(get()) }
}