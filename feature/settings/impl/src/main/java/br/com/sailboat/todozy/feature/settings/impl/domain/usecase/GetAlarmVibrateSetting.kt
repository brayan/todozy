package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository

internal class GetAlarmVibrateSetting(
    private val settingsRepository: SettingsRepository
) : GetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke() = settingsRepository.getAlarmVibrate()
}
