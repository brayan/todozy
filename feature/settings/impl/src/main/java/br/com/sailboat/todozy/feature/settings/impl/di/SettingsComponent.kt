package br.com.sailboat.todozy.feature.settings.impl.di

import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSource
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSourceImpl
import br.com.sailboat.todozy.feature.settings.impl.data.repository.SettingsRepositoryImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.*
import br.com.sailboat.todozy.feature.settings.impl.presentation.navigator.SettingsNavigatorImpl
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewModel
import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
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
    factory<SetAlarmSoundSettingUseCase> { SetAlarmSoundSetting(get()) }
    factory<GetAlarmSoundSettingUseCase> { GetAlarmSoundSetting(get()) }
    factory<SetAlarmVibrateSettingUseCase> { SetAlarmVibrateSetting(get()) }
    factory<GetAlarmVibrateSettingUseCase> { GetAlarmVibrateSetting(get()) }
    factory<CheckAndSetUpInitialSettingsUseCase> { CheckAndSetUpInitialSettings(get(), get()) }
}

private val data = module {
    factory<SettingsRepository> { SettingsRepositoryImpl(get()) }
    factory<SettingsLocalDataSource> { SettingsLocalDataSourceImpl(get()) }
}

val settingsComponent: List<Module> = listOf(presentation, domain, data)