package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class ScheduleAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(alarm: Alarm, taskId: Long) = alarmRepository.scheduleAlarm(alarm, taskId)

}