package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.ui.task.history.TaskHistoryContract
import br.com.sailboat.todozy.ui.task.history.TaskHistoryPresenter
import br.com.sailboat.todozy.ui.task.details.TaskDetailsContract
import br.com.sailboat.todozy.ui.task.details.TaskDetailsPresenter
import br.com.sailboat.todozy.ui.task.history.GetHistoryView
import br.com.sailboat.todozy.ui.task.insert.InsertTaskContract
import br.com.sailboat.todozy.ui.task.insert.InsertTaskPresenter
import br.com.sailboat.todozy.ui.task.list.GetTasksView
import br.com.sailboat.todozy.ui.task.list.TaskListContract
import br.com.sailboat.todozy.ui.task.list.TaskListPresenter
import org.koin.dsl.module

val uiModule = module {
    factory<TaskListContract.Presenter> { TaskListPresenter(get(), get(), get(), get()) }
    factory<InsertTaskContract.Presenter> { InsertTaskPresenter(get(), get()) }
    factory<TaskDetailsContract.Presenter> { TaskDetailsPresenter(get(), get(), get()) }
    factory<TaskHistoryContract.Presenter> { TaskHistoryPresenter(get(), get()) }

    factory { GetTasksView(get(), get()) }
    factory { GetHistoryView(get(), get()) }
}