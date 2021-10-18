package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

class GetAlarmSoundSetting(
    private val settingsRepository: SettingsRepository,
) : GetAlarmSoundSettingUseCase {

    override suspend operator fun invoke() = settingsRepository.getAlarmTone()

}