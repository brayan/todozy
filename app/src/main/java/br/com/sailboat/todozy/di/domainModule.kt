package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.domain.alarm.DeleteAlarm
import br.com.sailboat.todozy.domain.alarm.GetAlarm
import br.com.sailboat.todozy.domain.alarm.SetNextValidAlarm
import br.com.sailboat.todozy.domain.alarm.UpdateAlarm
import br.com.sailboat.todozy.domain.history.AddHistory
import br.com.sailboat.todozy.domain.history.GetTasksHistory
import br.com.sailboat.todozy.domain.tasks.*
import br.com.sailboat.todozy.ui.task.details.GetTaskDetailsView
import br.com.sailboat.todozy.ui.task.list.GetTasksView
import org.koin.dsl.module

val domainModule = module {
    factory { GetAlarm(get()) }
    factory { GetTask(get()) }
    factory { GetTasks(get()) }
    factory { GetTasksHistory() }
    factory { GetTaskDetailsView(get(), get()) }
    factory { GetTaskMetrics(get()) }
    factory { GetTasksView(get(), get()) }
    factory { DisableTask(get(), get()) }
    factory { MarkTask(get(), get(), get(), get(), get(), get()) }
    factory { DeleteAlarm(get()) }
    factory { SetNextValidAlarm(get()) }
    factory { AddHistory() }
    factory { UpdateAlarm() }
    factory { SaveTask(get()) }
}