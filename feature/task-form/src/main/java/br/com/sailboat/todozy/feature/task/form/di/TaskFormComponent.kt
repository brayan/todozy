package br.com.sailboat.todozy.feature.task.form.di

import br.com.sailboat.todozy.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.CheckTaskFields
import br.com.sailboat.todozy.feature.task.form.domain.usecase.CheckTaskFieldsUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTask
import br.com.sailboat.todozy.feature.task.form.presentation.TaskFormContract
import br.com.sailboat.todozy.feature.task.form.presentation.TaskFormPresenter
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<TaskFormContract.Presenter> { TaskFormPresenter(get(), get(), get(), get()) }
}

private val domain = module {
    factory<CheckTaskFieldsUseCase> { CheckTaskFields() }
    factory<SaveTaskUseCase> { SaveTask(get(), get(), get(), get()) }
}

val taskFormComponent: List<Module> = listOf(presentation, domain)