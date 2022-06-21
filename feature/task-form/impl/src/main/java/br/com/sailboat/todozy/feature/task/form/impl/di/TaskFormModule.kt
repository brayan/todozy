package br.com.sailboat.todozy.feature.task.form.impl.di

import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.service.AlarmService
import br.com.sailboat.todozy.feature.task.form.impl.domain.service.AlarmServiceImpl
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFields
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFieldsUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.SaveTask
import br.com.sailboat.todozy.feature.task.form.impl.presentation.navigator.TaskFormNavigatorImpl
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    viewModel {
        TaskFormViewModel(
            getTaskUseCase = get(),
            saveTaskUseCase = get(),
            getNextAlarmUseCase = get(),
            checkTaskFieldsUseCase = get(),
            logService = get(),
            alarmService = get(),
        )
    }
    factory<TaskFormNavigator> { TaskFormNavigatorImpl() }
    factory<AlarmService> { AlarmServiceImpl(get()) }
}

private val domain = module {
    factory<CheckTaskFieldsUseCase> { CheckTaskFields() }
    factory<SaveTaskUseCase> { SaveTask(get(), get(), get(), get()) }
}

val taskFormModule: List<Module> = listOf(presentation, domain)
