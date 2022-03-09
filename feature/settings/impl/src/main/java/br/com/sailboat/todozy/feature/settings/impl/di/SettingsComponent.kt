package br.com.sailboat.todozy.feature.settings.impl.di

import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSource
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSourceImpl
import br.com.sailboat.todozy.feature.settings.impl.data.repository.SettingsRepositoryImpl
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.*
import br.com.sailboat.todozy.feature.settings.impl.presentation.SettingsContract
import br.com.sailboat.todozy.feature.settings.impl.presentation.SettingsPresenter
import br.com.sailboat.todozy.feature.settings.impl.presentation.navigator.SettingsNavigatorImpl
import br.com.sailboat.todozy.feature.settings.presentation.navigator.SettingsNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<SettingsContract.Presenter> { SettingsPresenter(get(), get(), get(), get()) }
    single<SettingsNavigator> { SettingsNavigatorImpl() }
}

private val domain = module {
    factory<SetAlarmSoundSettingUseCase> { SetAlarmSoundSetting(get()) }
    factory<GetAlarmSoundSettingUseCase> { GetAlarmSoundSetting(get()) }
    factory<SetAlarmVibrateSettingUseCase> { SetAlarmVibrateSetting(get()) }
    factory<GetAlarmVibrateSettingUseCase> { GetAlarmVibrateSetting(get()) }
    factory<CheckAndSetUpInitialSettingsUseCase> { CheckAndSetUpInitialSettings(get(), get()) }
}

private val data = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<SettingsLocalDataSource> { SettingsLocalDataSourceImpl(get()) }
}

val settingsComponent: List<Module> = listOf(presentation, domain, data)