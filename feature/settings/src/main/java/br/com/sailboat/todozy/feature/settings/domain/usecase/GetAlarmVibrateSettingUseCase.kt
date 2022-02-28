package br.com.sailboat.todozy.feature.settings.domain.usecase

interface GetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(): Boolean
}