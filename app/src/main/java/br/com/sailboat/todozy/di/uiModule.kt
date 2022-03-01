package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.features.tasks.presentation.details.GetTaskDetailsView
import br.com.sailboat.todozy.features.tasks.presentation.details.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.features.tasks.presentation.details.TaskDetailsContract
import br.com.sailboat.todozy.features.tasks.presentation.details.TaskDetailsPresenter
import org.koin.dsl.module

val uiModule = module {
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