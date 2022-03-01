package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory<GetAlarmUseCase> { GetAlarm(get()) }
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTasksUseCase> { GetTasks(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
    factory<CompleteTaskUseCase> { CompleteTask(get(), get(), get(), get(), get()) }
    factory<DeleteAlarmUseCase> { DeleteAlarm(get(), get()) }
    factory<GetNextAlarmUseCase> { GetNextAlarm() }
}