package br.com.sailboat.todozy.feature.task.history.impl.di

import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.impl.data.datasource.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.history.impl.data.repository.TaskHistoryRepositoryImpl
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.*
import br.com.sailboat.todozy.feature.task.history.impl.presentation.*
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryToTaskHistoryUiModelMapper
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.feature.task.history.impl.presentation.navigator.TaskHistoryNavigatorImpl
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewModel
import br.com.sailboat.todozy.feature.task.history.presentation.navigator.TaskHistoryNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<TaskHistoryContract.Presenter> {
        TaskHistoryPresenter(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    factory<GetHistoryViewUseCase> { GetHistoryView(get(), get()) }
    factory<GetShortDateViewUseCase> { GetShortDateView(get()) }
    factory<GetDateFilterNameViewUseCase> { GetDateFilterNameView(get()) }

    single<TaskHistoryNavigator> { TaskHistoryNavigatorImpl() }
    single { TaskHistoryToTaskHistoryUiModelMapper() }
    single { TaskHistoryUiModelToTaskHistoryMapper() }

    viewModel { TaskHistoryViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}

private val domain = module {
    factory<GetTaskHistoryUseCase> { GetTaskHistory(get()) }
    factory<AddHistoryUseCase> { AddHistory(get()) }
    factory<UpdateHistoryUseCase> { UpdateHistory(get()) }
    factory<DeleteHistoryUseCase> { DeleteHistory(get()) }
    factory<DeleteAllHistoryUseCase> { DeleteAllHistory(get()) }
}

private val data = module {
    single<TaskHistoryRepository> { TaskHistoryRepositoryImpl(get()) }
    single<TaskHistoryLocalDataSource> { TaskHistoryLocalDataSourceSQLite(get()) }
}

val taskHistoryComponent: List<Module> = listOf(presentation, domain, data)
