package br.com.sailboat.todozy.feature.task.details.impl.di

import br.com.sailboat.todozy.feature.navigation.android.TaskDetailsNavigator
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.DisableTaskUseCaseImpl
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.GetTaskMetricsUseCaseImpl
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.GetTaskUseCaseImpl
import br.com.sailboat.todozy.feature.task.details.impl.presentation.factory.TaskDetailsUiModelFactory
import br.com.sailboat.todozy.feature.task.details.impl.presentation.navigator.TaskDetailsNavigatorImpl
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation =
    module {
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

private val domain =
    module {
        factory<GetTaskUseCase> { GetTaskUseCaseImpl(get()) }
        factory<GetTaskMetricsUseCase> { GetTaskMetricsUseCaseImpl(get()) }
        factory<DisableTaskUseCase> { DisableTaskUseCaseImpl(get(), get()) }
    }

val taskDetailsModule: List<Module> = listOf(presentation, domain)
