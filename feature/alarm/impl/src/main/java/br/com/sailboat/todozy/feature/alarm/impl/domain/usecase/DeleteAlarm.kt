package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository

class DeleteAlarm(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase,
) : br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase {

    override suspend operator fun invoke(taskId: Long) {
        alarmRepository.deleteAlarmByTask(taskId)
        cancelAlarmScheduleUseCase(taskId)
    }

}