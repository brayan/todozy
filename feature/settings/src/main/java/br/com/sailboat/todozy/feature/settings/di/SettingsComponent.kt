package br.com.sailboat.todozy.feature.settings.di

import br.com.sailboat.todozy.feature.settings.data.datasource.SettingsLocalDataSource
import br.com.sailboat.todozy.feature.settings.data.datasource.SettingsLocalDataSourceImpl
import br.com.sailboat.todozy.feature.settings.data.repository.SettingsRepositoryImpl
import br.com.sailboat.todozy.feature.settings.domain.repository.SettingsRepository
import br.com.sailboat.todozy.feature.settings.domain.usecase.*
import br.com.sailboat.todozy.feature.settings.presentation.SettingsContract
import br.com.sailboat.todozy.feature.settings.presentation.SettingsPresenter
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<SettingsContract.Presenter> { SettingsPresenter(get(), get(), get(), get()) }
}

private val domain = module {
    factory<SetAlarmSoundSettingUseCase> { SetAlarmSoundSetting(get()) }
    factory<GetAlarmSoundSettingUseCase> { GetAlarmSoundSetting(get()) }
    factory<SetAlarmVibrateSettingUseCase> { SetAlarmVibrateSetting(get()) }
    factory<GetAlarmVibrateSettingUseCase> { GetAlarmVibrateSetting(get()) }
    factory<CheckAndSetUpInitialSettingsUseCase> { CheckAndSetUpInitialSettings(get()) }
}

private val data = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<SettingsLocalDataSource> { SettingsLocalDataSourceImpl(get()) }
}

val settingsComponent: List<Module> = listOf(presentation, domain, data)