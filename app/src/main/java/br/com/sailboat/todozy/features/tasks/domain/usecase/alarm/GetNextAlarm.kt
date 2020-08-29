package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class GetNextAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(alarm: Alarm) = alarmRepository.getNextValidAlarm(alarm)

}