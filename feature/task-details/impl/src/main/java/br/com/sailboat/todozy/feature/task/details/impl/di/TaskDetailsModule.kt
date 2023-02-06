package br.com.sailboat.todozy.feature.task.details.impl.di

import br.com.sailboat.todozy.feature.navigation.android.TaskDetailsNavigator
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.DisableTask
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.GetTask
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.GetTaskMetrics
import br.com.sailboat.todozy.feature.task.details.impl.presentation.factory.TaskDetailsUiModelFactory
import br.com.sailboat.todozy.feature.task.details.impl.presentation.navigator.TaskDetailsNavigatorImpl
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    viewModel {
        TaskDetailsViewModel(
            getTaskMetricsUseCase = get(),
            getTaskUseCase = get(),
            disableTaskUseCase = get(),
            taskDetailsUiModelFactory = get(),
            logService = get(),
        )
    }

    factory { TaskDetailsUiModelFactory(get(), get()) }
    factory<TaskDetailsNavigator> { TaskDetailsNavigatorImpl() }
}

private val domain = module {
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get(), get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
}

val taskDetailsModule: List<Module> = listOf(presentation, domain)
