package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialCalendarForTomorrow

internal class ScheduleAlarmUpdatesUseCaseImpl(
    private val alarmManagerService: AlarmManagerService,
) : ScheduleAlarmUpdatesUseCase {
    override suspend operator fun invoke() {
        val calendar = getInitialCalendarForTomorrow()
        alarmManagerService.scheduleAlarmUpdates(calendar)
    }
}
