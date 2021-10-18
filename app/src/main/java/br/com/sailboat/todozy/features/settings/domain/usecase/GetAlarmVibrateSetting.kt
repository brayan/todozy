package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

class GetAlarmVibrateSetting(
    private val settingsRepository: SettingsRepository
) : GetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke() = settingsRepository.getAlarmVibrate()

}