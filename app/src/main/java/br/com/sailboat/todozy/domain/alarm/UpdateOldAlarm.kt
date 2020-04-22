package br.com.sailboat.todozy.domain.alarm

import br.com.sailboat.todozy.domain.helper.incrementToNextValidDate
import br.com.sailboat.todozy.domain.helper.isBeforeNow
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.AlarmRepository

class UpdateOldAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(alarm: Alarm, task: Task): Alarm {

        if (alarm.dateTime.isBeforeNow()) {
            alarm.dateTime.incrementToNextValidDate(alarm.repeatType, alarm.customDays)
        }

        alarmRepository.setAlarm(alarm, task)

        return alarm
    }

}