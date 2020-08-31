package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class DeleteAlarm(private val alarmRepository: AlarmRepository,
                  private val cancelAlarmSchedule: CancelAlarmSchedule) {

    suspend operator fun invoke(taskId: Long) {
        alarmRepository.deleteAlarmByTask(taskId)
        cancelAlarmSchedule(taskId)
    }

}