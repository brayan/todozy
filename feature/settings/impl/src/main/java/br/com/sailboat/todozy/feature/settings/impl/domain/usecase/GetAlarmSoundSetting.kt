package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository

class GetAlarmSoundSetting(
    private val settingsRepository: SettingsRepository,
) : GetAlarmSoundSettingUseCase {

    override suspend operator fun invoke() = settingsRepository.getAlarmTone()
}
