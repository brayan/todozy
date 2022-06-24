package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService

internal class CancelAlarmSchedule(
    private val alarmManagerService: AlarmManagerService,
) : CancelAlarmScheduleUseCase {

    override suspend operator fun invoke(taskId: Long) {
        alarmManagerService.cancelAlarm(taskId)
    }
}
