package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

class GetAlarmSoundSetting(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke() = settingsRepository.getAlarmTone()

}