package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository

class DeleteAlarm(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase,
) : DeleteAlarmUseCase {

    override suspend operator fun invoke(taskId: Long) {
        alarmRepository.deleteAlarmByTask(taskId)
        cancelAlarmScheduleUseCase(taskId)
    }

}