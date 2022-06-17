package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import android.net.Uri

interface GetAlarmSoundSettingUseCase {
    suspend operator fun invoke(): Uri?
}
