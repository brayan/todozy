package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.settings.domain.usecase.CheckAndSetUpInitialSettingsUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository

internal class CheckAndSetUpInitialSettingsUseCaseImpl(
    private val repository: SettingsRepository,
    private val scheduleAlarmUpdatesUseCase: ScheduleAlarmUpdatesUseCase,
) : CheckAndSetUpInitialSettingsUseCase {
    override suspend operator fun invoke() = with(repository) {
        if (isFirstTimeLaunchingApp().not()) {
            return
        }

        setFirstTimeLaunchingApp(false)
        setAlarmVibrate(true)
        setDefaultAlarmTone()
        scheduleAlarmUpdatesUseCase()
    }
}
