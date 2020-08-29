package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.extensions.incrementToNextValidDate
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm

class GetNextAlarm {

    operator fun invoke(alarm: Alarm): Alarm {
        return alarm.apply { dateTime.incrementToNextValidDate(alarm.repeatType, alarm.customDays) }
    }

}