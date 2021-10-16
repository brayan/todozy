package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class CancelAlarmSchedule(
    private val alarmRepository: AlarmRepository,
) : CancelAlarmScheduleUseCase {

    override suspend operator fun invoke(taskId: Long) {
        alarmRepository.cancelAlarmSchedule(taskId)
    }

}