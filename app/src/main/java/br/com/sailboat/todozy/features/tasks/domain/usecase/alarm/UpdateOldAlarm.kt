package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.extensions.incrementToNextValidDate
import br.com.sailboat.todozy.core.extensions.isBeforeNow
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class UpdateOldAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(alarm: Alarm, task: Task): Alarm {

        if (alarm.dateTime.isBeforeNow()) {
            alarm.dateTime.incrementToNextValidDate(alarm.repeatType, alarm.customDays)
        }

        alarmRepository.setAlarm(alarm, task)

        return alarm
    }

}