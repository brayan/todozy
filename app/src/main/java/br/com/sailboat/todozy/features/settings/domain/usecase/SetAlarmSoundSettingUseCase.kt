package br.com.sailboat.todozy.features.settings.domain.usecase

import android.net.Uri

interface SetAlarmSoundSettingUseCase {
    suspend operator fun invoke(soundUri: Uri)
}