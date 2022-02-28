package br.com.sailboat.todozy.feature.settings.domain.usecase

import br.com.sailboat.todozy.feature.settings.domain.repository.SettingsRepository

class SetAlarmVibrateSetting(
    private val settingsRepository: SettingsRepository,
) : SetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke(vibrate: Boolean) {
        settingsRepository.setAlarmVibrate(vibrate)
    }

}