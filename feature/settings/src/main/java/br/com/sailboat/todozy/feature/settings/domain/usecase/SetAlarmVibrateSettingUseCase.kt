package br.com.sailboat.todozy.feature.settings.domain.usecase

interface SetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(vibrate: Boolean)
}