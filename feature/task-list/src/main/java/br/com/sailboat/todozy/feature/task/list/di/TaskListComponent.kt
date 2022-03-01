package br.com.sailboat.todozy.feature.task.list.di

import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.task.list.data.datasource.TaskLocalDataSource
import br.com.sailboat.todozy.feature.task.list.data.datasource.TaskLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.list.data.repository.TaskRepositoryImpl
import br.com.sailboat.todozy.feature.task.list.domain.usecase.CompleteTask
import br.com.sailboat.todozy.feature.task.list.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasks
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.feature.task.list.presentation.GetTasksView
import br.com.sailboat.todozy.feature.task.list.presentation.GetTasksViewUseCase
import br.com.sailboat.todozy.feature.task.list.presentation.TaskToTaskUiModelMapper
import br.com.sailboat.todozy.feature.task.list.presentation.viewmodel.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<GetTasksViewUseCase> { GetTasksView(get(), get()) }

    single { TaskToTaskUiModelMapper() }

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
}

private val domain = module {
    factory<GetTasksUseCase> { GetTasks(get()) }
    factory<CompleteTaskUseCase> { CompleteTask(get(), get(), get(), get(), get()) }
}

private val data = module {
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<TaskLocalDataSource> { TaskLocalDataSourceSQLite(get()) }
}

val taskListComponent: List<Module> = listOf(presentation, domain, data)