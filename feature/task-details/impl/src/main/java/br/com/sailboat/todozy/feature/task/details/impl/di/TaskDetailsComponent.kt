package br.com.sailboat.todozy.feature.task.details.impl.di

import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.DisableTask
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.GetTask
import br.com.sailboat.todozy.feature.task.details.impl.domain.usecase.GetTaskMetrics
import br.com.sailboat.todozy.feature.task.details.impl.presentation.GetTaskDetailsView
import br.com.sailboat.todozy.feature.task.details.impl.presentation.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.TaskDetailsContract
import br.com.sailboat.todozy.feature.task.details.impl.presentation.TaskDetailsPresenter
import br.com.sailboat.todozy.feature.task.details.impl.presentation.navigator.TaskDetailsNavigatorImpl
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.navigator.TaskDetailsNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<TaskDetailsContract.Presenter> {
        TaskDetailsPresenter(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    factory<GetTaskDetailsViewUseCase> { GetTaskDetailsView(get(), get(), get()) }
    factory<TaskDetailsNavigator> { TaskDetailsNavigatorImpl() }
}

private val domain = module {
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
}

val taskDetailsComponent: List<Module> = listOf(presentation, domain)