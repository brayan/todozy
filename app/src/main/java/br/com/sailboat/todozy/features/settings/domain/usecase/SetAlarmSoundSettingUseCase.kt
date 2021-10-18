package br.com.sailboat.todozy.features.settings.domain.usecase

import android.net.Uri
import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository

interface SetAlarmSoundSettingUseCase {
    suspend operator fun invoke(soundUri: Uri)
}