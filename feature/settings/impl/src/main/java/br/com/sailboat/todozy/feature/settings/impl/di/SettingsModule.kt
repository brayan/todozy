package br.com.sailboat.todozy.feature.settings.impl.di

import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.settings.android.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.domain.usecase.CheckAndSetUpInitialSettingsUseCase
import br.com.sailboat.todozy.feature.settings.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSource
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSourceImpl
import br.com.sailboat.todozy.feature.settings.impl.data.repository.SettingsRepositoryImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.CheckAndSetUpInitialSettingsUseCaseImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmSoundSettingUseCaseImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmVibrateSettingUseCaseImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSettingUseCaseImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSettingUseCaseImpl
import br.com.sailboat.todozy.feature.settings.impl.presentation.navigator.SettingsNavigatorImpl
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    viewModel {
        SettingsViewModel(
            getAlarmSoundSettingUseCase = get(),
            setAlarmSoundSettingUseCase = get(),
            getAlarmVibrateSettingUseCase = get(),
            setAlarmVibrateSettingUseCase = get(),
        )
    }
    factory<SettingsNavigator> { SettingsNavigatorImpl() }
}

private val domain = module {
    factory<SetAlarmSoundSettingUseCase> { SetAlarmSoundSettingUseCaseImpl(get()) }
    factory<GetAlarmSoundSettingUseCase> { GetAlarmSoundSettingUseCaseImpl(get()) }
    factory<SetAlarmVibrateSettingUseCase> { SetAlarmVibrateSettingUseCaseImpl(get()) }
    factory<GetAlarmVibrateSettingUseCase> { GetAlarmVibrateSettingUseCaseImpl(get()) }
    factory<CheckAndSetUpInitialSettingsUseCase> { CheckAndSetUpInitialSettingsUseCaseImpl(get(), get()) }
}

private val data = module {
    factory<SettingsRepository> { SettingsRepositoryImpl(get()) }
    factory<SettingsLocalDataSource> { SettingsLocalDataSourceImpl(get()) }
}

val settingsModule: List<Module> = listOf(presentation, domain, data)
