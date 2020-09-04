package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAlarmUpdates

class CheckAndSetUpInitialSettings(private val repository: SettingsRepository,
                                   private val scheduleAlarmUpdates: ScheduleAlarmUpdates) {

    suspend operator fun invoke() = with(repository) {
        if (isFirstTimeLaunchingApp().not()) {
            return
        }

        setFirstTimeLaunchingApp(false)
        setAlarmVibrate(true)
        setDefaultAlarmTone()
        scheduleAlarmUpdates()
    }

}