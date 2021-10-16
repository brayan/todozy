package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class DeleteAlarm(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase,
) {

    suspend operator fun invoke(taskId: Long) {
        alarmRepository.deleteAlarmByTask(taskId)
        cancelAlarmScheduleUseCase(taskId)
    }

}