package br.com.sailboat.todozy.core.di

import br.com.sailboat.todozy.features.tasks.presentation.details.TaskDetailsContract
import br.com.sailboat.todozy.features.tasks.presentation.details.TaskDetailsPresenter
import br.com.sailboat.todozy.features.tasks.presentation.form.InsertTaskContract
import br.com.sailboat.todozy.features.tasks.presentation.form.InsertTaskPresenter
import br.com.sailboat.todozy.features.tasks.presentation.history.*
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksView
import br.com.sailboat.todozy.features.tasks.presentation.list.TaskListContract
import br.com.sailboat.todozy.features.tasks.presentation.list.TaskListPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory<TaskListContract.Presenter> { TaskListPresenter(get(), get(), get(), get()) }
    factory<InsertTaskContract.Presenter> { InsertTaskPresenter(get(), get()) }
    factory<TaskDetailsContract.Presenter> { TaskDetailsPresenter(get(), get(), get(), get(), get()) }
    factory<TaskHistoryContract.Presenter> { TaskHistoryPresenter(get(), get(), get(), get(), get()) }

    factory { GetTasksView(get(), get()) }
    factory { GetHistoryView(get(), get()) }
    factory { GetShortDateView(get()) }
    factory { GetDateFilterNameView(get()) }

    viewModel { TaskHistoryViewModel(get(), get(), get(), get(), get(), get(), get()) }
}