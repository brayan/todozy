package br.com.sailboat.todozy.feature.settings.domain.usecase

import br.com.sailboat.todozy.feature.settings.domain.repository.SettingsRepository

class GetAlarmVibrateSetting(
    private val settingsRepository: SettingsRepository
) : GetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke() = settingsRepository.getAlarmVibrate()

}