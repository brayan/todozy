package br.com.sailboat.todozy.domain.alarm

import br.com.sailboat.todozy.domain.repository.AlarmRepository

class GetAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(taskId: Long) = alarmRepository.getAlarmByTaskId(taskId)

}