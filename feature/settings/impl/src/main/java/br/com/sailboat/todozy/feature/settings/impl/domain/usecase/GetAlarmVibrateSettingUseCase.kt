package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

interface GetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(): Boolean
}