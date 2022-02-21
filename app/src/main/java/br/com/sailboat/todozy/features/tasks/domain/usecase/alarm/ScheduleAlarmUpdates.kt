package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.platform.AlarmManagerService
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialCalendarForTomorrow

class ScheduleAlarmUpdates(
    private val alarmManagerService: AlarmManagerService,
) : ScheduleAlarmUpdatesUseCase {

    override suspend operator fun invoke() {
        val calendar = getInitialCalendarForTomorrow()
        alarmManagerService.scheduleAlarmUpdates(calendar)
    }

}