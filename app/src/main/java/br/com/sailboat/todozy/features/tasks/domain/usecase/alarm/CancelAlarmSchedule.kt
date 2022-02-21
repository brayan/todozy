package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.platform.AlarmManagerService

class CancelAlarmSchedule(
    private val alarmManagerService: AlarmManagerService,
) : CancelAlarmScheduleUseCase {

    override suspend operator fun invoke(taskId: Long) {
        alarmManagerService.cancelAlarm(taskId)
    }

}