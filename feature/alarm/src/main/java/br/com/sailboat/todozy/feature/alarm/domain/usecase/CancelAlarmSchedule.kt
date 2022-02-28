package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService

class CancelAlarmSchedule(
    private val alarmManagerService: AlarmManagerService,
) : CancelAlarmScheduleUseCase {

    override suspend operator fun invoke(taskId: Long) {
        alarmManagerService.cancelAlarm(taskId)
    }

}