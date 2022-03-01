package br.com.sailboat.todozy.feature.settings.domain.usecase

import br.com.sailboat.todozy.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.settings.domain.repository.SettingsRepository

class CheckAndSetUpInitialSettings(
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