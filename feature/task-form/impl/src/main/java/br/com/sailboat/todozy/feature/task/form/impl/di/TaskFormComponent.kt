package br.com.sailboat.todozy.feature.task.form.impl.di

import br.com.sailboat.todozy.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFields
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFieldsUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.SaveTask
import br.com.sailboat.todozy.feature.task.form.impl.presentation.TaskFormContract
import br.com.sailboat.todozy.feature.task.form.impl.presentation.TaskFormPresenter
import br.com.sailboat.todozy.feature.task.form.impl.presentation.navigator.TaskFormNavigatorImpl
import br.com.sailboat.todozy.feature.task.form.presentation.navigator.TaskFormNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<TaskFormContract.Presenter> { TaskFormPresenter(get(), get(), get(), get()) }
    single<TaskFormNavigator> { TaskFormNavigatorImpl() }
}

private val domain = module {
    factory<CheckTaskFieldsUseCase> { CheckTaskFields() }
    factory<SaveTaskUseCase> { SaveTask(get(), get(), get(), get()) }
}

val taskFormComponent: List<Module> = listOf(presentation, domain)