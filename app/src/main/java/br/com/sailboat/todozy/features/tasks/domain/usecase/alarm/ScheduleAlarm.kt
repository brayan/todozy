package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.platform.AlarmManagerService
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm

class ScheduleAlarm(
    private val alarmManagerService: AlarmManagerService,
) : ScheduleAlarmUseCase {

    override suspend operator fun invoke(alarm: Alarm, taskId: Long) {
        alarmManagerService.scheduleAlarm(alarm.dateTime, taskId)
    }

}