package br.com.sailboat.todozy.features.settings.domain.usecase

interface SetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(vibrate: Boolean)
}