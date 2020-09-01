package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmVibrateSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.SetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.SetAlarmVibrateSetting
import br.com.sailboat.todozy.features.tasks.domain.usecase.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.*
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
    factory { DeleteAlarm(get(), get()) }
    factory { GetNextAlarm() }
    factory { AddHistory(get()) }
    factory { UpdateHistory(get()) }
    factory { DeleteHistory(get()) }
    factory { DeleteAllHistory(get()) }
    factory { SaveTask(get(), get(), get()) }
    factory { SetAlarmSoundSetting(get()) }
    factory { GetAlarmSoundSetting(get()) }
    factory { SetAlarmVibrateSetting(get()) }
    factory { GetAlarmVibrateSetting(get()) }
    factory { ScheduleAlarm(get()) }
    factory { ScheduleAllAlarms(get(), get(), get()) }
    factory { ScheduleAlarmUpdates(get()) }
    factory { CancelAlarmSchedule(get()) }
    factory { SaveAlarm(get(), get(), get()) }
}