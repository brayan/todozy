package br.com.sailboat.todozy.feature.task.form.impl.di

import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.service.AlarmService
import br.com.sailboat.todozy.feature.task.form.impl.domain.service.AlarmServiceImpl
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFieldsUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFieldsUseCaseImpl
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.SaveTaskUseCaseImpl
import br.com.sailboat.todozy.feature.task.form.impl.presentation.formatter.TaskFormDateTimeFormatter
import br.com.sailboat.todozy.feature.task.form.impl.presentation.formatter.TaskFormDateTimeFormatterImpl
import br.com.sailboat.todozy.feature.task.form.impl.presentation.navigator.TaskFormNavigatorImpl
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewModel
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import br.com.sailboat.uicomponent.impl.helper.WeekDaysHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation =
    module {
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
        factory<TaskFormDateTimeFormatter> { TaskFormDateTimeFormatterImpl(get()) }
        factory { WeekDaysHelper(get<StringProvider>()) }
        factory<AlarmService> { AlarmServiceImpl(get(), get(), get()) }
    }

private val domain =
    module {
        factory<CheckTaskFieldsUseCase> { CheckTaskFieldsUseCaseImpl() }
        factory<SaveTaskUseCase> { SaveTaskUseCaseImpl(get(), get(), get(), get()) }
    }

val taskFormModule: List<Module> = listOf(presentation, domain)
