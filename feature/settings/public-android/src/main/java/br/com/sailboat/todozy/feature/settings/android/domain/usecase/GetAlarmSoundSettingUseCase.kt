package br.com.sailboat.todozy.feature.settings.android.domain.usecase

import android.net.Uri

interface GetAlarmSoundSettingUseCase {
    suspend operator fun invoke(): Uri?
}
