package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.features.settings.domain.usecase.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.*
import org.koin.dsl.module

val domainModule = module {
    factory<CheckTaskFieldsUseCase> { CheckTaskFields() }
    factory { GetAlarm(get()) }
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTasksUseCase> { GetTasks(get()) }
    factory { GetTaskHistory(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
    factory<CompleteTaskUseCase> { CompleteTask(get(), get(), get(), get(), get()) }
    factory { DeleteAlarm(get(), get()) }
    factory { GetNextAlarm() }
    factory<AddHistoryUseCase> { AddHistory(get()) }
    factory { UpdateHistory(get()) }
    factory { DeleteHistory(get()) }
    factory { DeleteAllHistory(get()) }
    factory<SaveTaskUseCase> { SaveTask(get(), get(), get(), get()) }
    factory { SetAlarmSoundSetting(get()) }
    factory { GetAlarmSoundSetting(get()) }
    factory { SetAlarmVibrateSetting(get()) }
    factory { GetAlarmVibrateSetting(get()) }
    factory { ScheduleAlarm(get()) }
    factory { ScheduleAllAlarms(get(), get(), get()) }
    factory { ScheduleAlarmUpdates(get()) }
    factory { CancelAlarmSchedule(get()) }
    factory { SaveAlarm(get(), get(), get()) }
    factory { CheckAndSetUpInitialSettings(get(), get()) }
}