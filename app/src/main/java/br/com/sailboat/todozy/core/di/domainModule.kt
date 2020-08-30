package br.com.sailboat.todozy.core.di

import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmVibrateSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.SetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.SetAlarmVibrateSetting
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetNextAlarm
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
    factory { GetNextAlarm(get()) }
    factory { AddHistory(get()) }
    factory { UpdateHistory(get()) }
    factory { DeleteHistory(get()) }
    factory { DeleteAllHistory(get()) }
    factory { SaveTask(get()) }
    factory { SetAlarmSoundSetting(get()) }
    factory { GetAlarmSoundSetting(get()) }
    factory { SetAlarmVibrateSetting(get()) }
    factory { GetAlarmVibrateSetting(get()) }
}