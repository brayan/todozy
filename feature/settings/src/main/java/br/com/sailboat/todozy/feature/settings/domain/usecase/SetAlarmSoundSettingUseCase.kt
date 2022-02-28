package br.com.sailboat.todozy.feature.settings.domain.usecase

import android.net.Uri

interface SetAlarmSoundSettingUseCase {
    suspend operator fun invoke(soundUri: Uri)
}