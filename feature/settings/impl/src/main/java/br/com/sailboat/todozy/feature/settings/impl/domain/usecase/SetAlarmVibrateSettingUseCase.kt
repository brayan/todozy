package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

internal interface SetAlarmVibrateSettingUseCase {
    suspend operator fun invoke(vibrate: Boolean)
}
