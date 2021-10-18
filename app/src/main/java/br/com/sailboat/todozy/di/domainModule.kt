package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.features.settings.domain.usecase.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.*
import org.koin.dsl.module

val domainModule = module {
    factory<CheckTaskFieldsUseCase> { CheckTaskFields() }
    factory<GetAlarmUseCase> { GetAlarm(get()) }
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTasksUseCase> { GetTasks(get()) }
    factory<GetTaskHistoryUseCase> { GetTaskHistory(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
    factory<CompleteTaskUseCase> { CompleteTask(get(), get(), get(), get(), get()) }
    factory<DeleteAlarmUseCase> { DeleteAlarm(get(), get()) }
    factory<GetNextAlarmUseCase> { GetNextAlarm() }
    factory<AddHistoryUseCase> { AddHistory(get()) }
    factory<UpdateHistoryUseCase> { UpdateHistory(get()) }
    factory<DeleteHistoryUseCase> { DeleteHistory(get()) }
    factory<DeleteAllHistoryUseCase> { DeleteAllHistory(get()) }
    factory<SaveTaskUseCase> { SaveTask(get(), get(), get(), get()) }
    factory { SetAlarmSoundSetting(get()) }
    factory<GetAlarmSoundSettingUseCase> { GetAlarmSoundSetting(get()) }
    factory { SetAlarmVibrateSetting(get()) }
    factory { GetAlarmVibrateSetting(get()) }
    factory<ScheduleAlarmUseCase> { ScheduleAlarm(get()) }
    factory<ScheduleAllAlarmsUseCase> { ScheduleAllAlarms(get(), get(), get()) }
    factory { ScheduleAlarmUpdates(get()) }
    factory<CancelAlarmScheduleUseCase> { CancelAlarmSchedule(get()) }
    factory<SaveAlarmUseCase> { SaveAlarm(get(), get(), get()) }
    factory<CheckAndSetUpInitialSettingsUseCase> { CheckAndSetUpInitialSettings(get(), get()) }
}