package br.com.sailboat.todozy.feature.task.history.di

import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.data.datasource.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.history.data.repository.TaskHistoryRepositoryImpl
import br.com.sailboat.todozy.feature.task.history.domain.usecase.*
import br.com.sailboat.todozy.feature.task.history.presentation.*
import br.com.sailboat.todozy.feature.task.history.presentation.mapper.TaskHistoryToTaskHistoryUiModelMapper
import br.com.sailboat.todozy.feature.task.history.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.feature.task.history.presentation.viewmodel.TaskHistoryViewModel
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