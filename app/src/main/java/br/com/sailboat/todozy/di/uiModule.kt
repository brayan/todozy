package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.features.about.presentation.AboutContract
import br.com.sailboat.todozy.features.about.presentation.AboutPresenter
import br.com.sailboat.todozy.features.settings.presentation.SettingsContract
import br.com.sailboat.todozy.features.settings.presentation.SettingsPresenter
import br.com.sailboat.todozy.features.tasks.presentation.details.GetTaskDetailsView
import br.com.sailboat.todozy.features.tasks.presentation.details.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.features.tasks.presentation.details.TaskDetailsContract
import br.com.sailboat.todozy.features.tasks.presentation.details.TaskDetailsPresenter
import br.com.sailboat.todozy.features.tasks.presentation.form.TaskFormContract
import br.com.sailboat.todozy.features.tasks.presentation.form.TaskFormPresenter
import br.com.sailboat.todozy.features.tasks.presentation.history.*
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksView
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksViewUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel.TaskListViewModel
import br.com.sailboat.todozy.features.tasks.presentation.mapper.AlarmToAlarmUiModelMapper
import br.com.sailboat.todozy.features.tasks.presentation.mapper.TaskToTaskUiModelMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory<TaskFormContract.Presenter> { TaskFormPresenter(get(), get(), get(), get()) }
    factory<TaskDetailsContract.Presenter> {
        TaskDetailsPresenter(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory<TaskHistoryContract.Presenter> {
        TaskHistoryPresenter(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory<SettingsContract.Presenter> { SettingsPresenter(get(), get(), get(), get()) }
    factory<AboutContract.Presenter> { AboutPresenter() }

    factory<GetTasksViewUseCase> { GetTasksView(get(), get()) }
    factory<GetTaskDetailsViewUseCase> { GetTaskDetailsView(get(), get(), get()) }
    factory<GetHistoryViewUseCase> { GetHistoryView(get()) }
    factory<GetShortDateViewUseCase> { GetShortDateView(get()) }
    factory<GetDateFilterNameViewUseCase> { GetDateFilterNameView(get()) }

    single { TaskToTaskUiModelMapper() }
    single { AlarmToAlarmUiModelMapper(get()) }

    viewModel {
        TaskListViewModel(
            getTasksViewUseCase = get(),
            getAlarmUseCase = get(),
            scheduleAllAlarmsUseCase = get(),
            getTaskMetricsUseCase = get(),
            completeTaskUseCase = get(),
            logService = get(),
        )
    }

    viewModel { TaskHistoryViewModel(get(), get(), get(), get(), get(), get(), get()) }
}