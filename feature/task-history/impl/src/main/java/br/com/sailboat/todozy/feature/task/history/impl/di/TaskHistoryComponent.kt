package br.com.sailboat.todozy.feature.task.history.impl.di

import br.com.sailboat.todozy.feature.navigation.android.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.impl.data.datasource.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.history.impl.data.repository.TaskHistoryRepositoryImpl
import br.com.sailboat.todozy.feature.task.history.impl.data.service.CalendarServiceImpl
import br.com.sailboat.todozy.feature.task.history.impl.domain.service.CalendarService
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.AddHistory
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteAllHistory
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteHistory
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.GetTaskHistory
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.GetTaskHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.UpdateHistory
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.UpdateHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetDateFilterNameView
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetDateFilterNameViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetHistoryView
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetHistoryViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryToTaskHistoryUiModelMapper
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.feature.task.history.impl.presentation.navigator.TaskHistoryNavigatorImpl
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<CalendarService> { CalendarServiceImpl(get()) }
    factory<GetHistoryViewUseCase> { GetHistoryView(get(), get(), get()) }
    factory<GetDateFilterNameViewUseCase> { GetDateFilterNameView(get()) }

    factory<TaskHistoryNavigator> { TaskHistoryNavigatorImpl() }
    factory { TaskHistoryToTaskHistoryUiModelMapper() }
    factory { TaskHistoryUiModelToTaskHistoryMapper() }

    viewModel {
        TaskHistoryViewModel(
            getTaskMetricsUseCase = get(),
            getHistoryViewUseCase = get(),
            getDateFilterNameViewUseCase = get(),
            updateHistoryUseCase = get(),
            deleteHistoryUseCase = get(),
            deleteAllHistoryUseCase = get(),
            taskHistoryUiModelToTaskHistoryMapper = get(),
            logService = get(),
            calendarService = get(),
        )
    }
}

private val domain = module {
    factory<GetTaskHistoryUseCase> { GetTaskHistory(get()) }
    factory<AddHistoryUseCase> { AddHistory(get()) }
    factory<UpdateHistoryUseCase> { UpdateHistory(get()) }
    factory<DeleteHistoryUseCase> { DeleteHistory(get()) }
    factory<DeleteAllHistoryUseCase> { DeleteAllHistory(get()) }
}

private val data = module {
    factory<TaskHistoryRepository> { TaskHistoryRepositoryImpl(get()) }
    factory<TaskHistoryLocalDataSource> { TaskHistoryLocalDataSourceSQLite(get()) }
}

val taskHistoryComponent: List<Module> = listOf(presentation, domain, data)
