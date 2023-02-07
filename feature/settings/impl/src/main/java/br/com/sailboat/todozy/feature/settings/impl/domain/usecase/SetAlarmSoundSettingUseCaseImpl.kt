package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import android.net.Uri
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository

internal class SetAlarmSoundSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository,
) : SetAlarmSoundSettingUseCase {

    override suspend operator fun invoke(soundUri: Uri) = settingsRepository.setAlarmTone(soundUri)
}
