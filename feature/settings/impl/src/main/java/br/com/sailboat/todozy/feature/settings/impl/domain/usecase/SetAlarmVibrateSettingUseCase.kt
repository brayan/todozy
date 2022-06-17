package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

interface SetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(vibrate: Boolean)
}
