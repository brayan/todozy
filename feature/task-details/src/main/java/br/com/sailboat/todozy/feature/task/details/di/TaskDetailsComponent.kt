package br.com.sailboat.todozy.feature.task.details.di

import br.com.sailboat.todozy.domain.usecase.*
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTask
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTask
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetrics
import br.com.sailboat.todozy.feature.task.details.presentation.GetTaskDetailsView
import br.com.sailboat.todozy.feature.task.details.presentation.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.TaskDetailsContract
import br.com.sailboat.todozy.feature.task.details.presentation.TaskDetailsPresenter
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
}

private val domain = module {
    factory<GetTaskUseCase> { GetTask(get()) }
    factory<GetTaskMetricsUseCase> { GetTaskMetrics(get()) }
    factory<DisableTaskUseCase> { DisableTask(get(), get()) }
}

val taskDetailsComponent: List<Module> = listOf(presentation, domain)