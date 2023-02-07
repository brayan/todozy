package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.settings.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository

internal class GetAlarmVibrateSettingUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetAlarmVibrateSettingUseCase {

    override suspend operator fun invoke() = settingsRepository.getAlarmVibrate()
}
