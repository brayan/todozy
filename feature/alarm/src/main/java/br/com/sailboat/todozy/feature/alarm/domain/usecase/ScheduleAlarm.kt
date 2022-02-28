package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService

class ScheduleAlarm(
    private val alarmManagerService: AlarmManagerService,
) : ScheduleAlarmUseCase {

    override suspend operator fun invoke(alarm: Alarm, taskId: Long) {
        alarmManagerService.scheduleAlarm(alarm.dateTime, taskId)
    }

}