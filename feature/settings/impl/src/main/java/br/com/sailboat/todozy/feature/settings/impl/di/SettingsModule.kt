package br.com.sailboat.todozy.feature.settings.impl.di

import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSource
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSourceImpl
import br.com.sailboat.todozy.feature.settings.impl.data.repository.SettingsRepositoryImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.CheckAndSetUpInitialSettings
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.CheckAndSetUpInitialSettingsUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmSoundSetting
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmVibrateSetting
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSetting
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSetting
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.SetAlarmVibrateSettingUseCase
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

val settingsModule: List<Module> = listOf(presentation, domain, data)
