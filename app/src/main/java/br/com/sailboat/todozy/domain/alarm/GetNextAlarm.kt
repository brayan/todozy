package br.com.sailboat.todozy.domain.alarm

import br.com.sailboat.todozy.domain.helper.incrementToNextValidDate
import br.com.sailboat.todozy.domain.helper.isBeforeNow
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.AlarmRepository
import java.util.*

class GetNextAlarm {

    operator fun invoke(alarm: Alarm): Alarm {
        alarm.dateTime.incrementToNextValidDate(alarm.repeatType, alarm.customDays)
        return alarm
    }

}