package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import android.net.Uri

internal interface SetAlarmSoundSettingUseCase {
    suspend operator fun invoke(soundUri: Uri)
}
