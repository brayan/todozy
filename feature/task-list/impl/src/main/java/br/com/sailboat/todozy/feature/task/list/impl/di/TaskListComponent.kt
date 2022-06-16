package br.com.sailboat.todozy.feature.task.list.impl.di

import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.navigation.android.TaskListNavigator
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.feature.task.list.impl.data.datasource.TaskLocalDataSource
import br.com.sailboat.todozy.feature.task.list.impl.data.datasource.TaskLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.list.impl.data.repository.TaskRepositoryImpl
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTask
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.GetTasks
import br.com.sailboat.todozy.feature.task.list.impl.presentation.factory.TaskListUiModelFactory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskCategoryToStringMapper
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskToTaskUiModelMapper
import br.com.sailboat.todozy.feature.task.list.impl.presentation.navigator.TaskListNavigatorImpl
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<TaskListNavigator> { TaskListNavigatorImpl() }
    factory { TaskListUiModelFactory(get(), get()) }

    factory { TaskToTaskUiModelMapper(get()) }
    factory { TaskCategoryToStringMapper(get()) }

    viewModel {
        TaskListViewModel(
            getTasksUseCase = get(),
            getAlarmUseCase = get(),
            scheduleAllAlarmsUseCase = get(),
            getTaskMetricsUseCase = get(),
            completeTaskUseCase = get(),
            taskListUiModelFactory = get(),
            logService = get(),
        )
    }
}

private val domain = module {
    factory<GetTasksUseCase> { GetTasks(get()) }
    factory<CompleteTaskUseCase> { CompleteTask(get(), get(), get(), get(), get()) }
}

private val data = module {
    factory<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    factory<TaskLocalDataSource> { TaskLocalDataSourceSQLite(get()) }
}

val taskListComponent: List<Module> = listOf(presentation, domain, data)