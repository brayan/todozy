package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.domain.usecase.*
import br.com.sailboat.todozy.feature.alarm.domain.usecase.*
import br.com.sailboat.todozy.feature.task.list.domain.usecase.CompleteTask
import br.com.sailboat.todozy.feature.task.list.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasks
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory<GetAlarmUseCase> { GetAlarm(get()) }
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
    factory<DeleteAlarmUseCase> { DeleteAlarm(get(), get()) }
    factory<GetNextAlarmUseCase> { GetNextAlarm() }
}