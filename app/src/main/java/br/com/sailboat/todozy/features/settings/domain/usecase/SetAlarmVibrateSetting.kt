package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

class SetAlarmVibrateSetting(
    private val settingsRepository: SettingsRepository,
) : SetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke(vibrate: Boolean) {
        settingsRepository.setAlarmVibrate(vibrate)
    }

}