package br.com.sailboat.todozy.features.settings.domain.usecase

interface GetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(): Boolean
}