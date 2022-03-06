package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository

class SetAlarmVibrateSetting(
    private val settingsRepository: SettingsRepository,
) : SetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke(vibrate: Boolean) {
        settingsRepository.setAlarmVibrate(vibrate)
    }

}