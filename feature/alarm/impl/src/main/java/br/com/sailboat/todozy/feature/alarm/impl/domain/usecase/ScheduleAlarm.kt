package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService

internal class ScheduleAlarm(
    private val alarmManagerService: AlarmManagerService,
) : ScheduleAlarmUseCase {

    override suspend operator fun invoke(alarm: Alarm, taskId: Long) {
        alarmManagerService.scheduleAlarm(alarm.dateTime, taskId)
    }
}
