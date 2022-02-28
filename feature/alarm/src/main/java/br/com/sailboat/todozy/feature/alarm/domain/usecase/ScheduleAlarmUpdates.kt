package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialCalendarForTomorrow

class ScheduleAlarmUpdates(
    private val alarmManagerService: AlarmManagerService,
) : ScheduleAlarmUpdatesUseCase {

    override suspend operator fun invoke() {
        val calendar = getInitialCalendarForTomorrow()
        alarmManagerService.scheduleAlarmUpdates(calendar)
    }

}