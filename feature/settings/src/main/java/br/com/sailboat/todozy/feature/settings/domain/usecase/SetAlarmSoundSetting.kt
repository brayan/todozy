package br.com.sailboat.todozy.feature.settings.domain.usecase

import android.net.Uri
import br.com.sailboat.todozy.feature.settings.domain.repository.SettingsRepository

class SetAlarmSoundSetting(
    private val settingsRepository: SettingsRepository,
) : SetAlarmSoundSettingUseCase {

    override suspend operator fun invoke(soundUri: Uri) = settingsRepository.setAlarmTone(soundUri)

}