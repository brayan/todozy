package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import java.util.*

class GetNextAlarm : br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase {

    override operator fun invoke(alarm: Alarm): Alarm {
        if (alarm.repeatType == RepeatType.NOT_REPEAT) {
            return alarm
        }

        do {
            incrementToNext(alarm.dateTime, alarm.repeatType, alarm.customDays)
        } while (alarm.dateTime.isBeforeNow())

        return alarm
    }

    private fun incrementToNext(calendar: Calendar, repeatType: RepeatType, days: String?) {
        when (repeatType) {
            RepeatType.SECOND -> calendar.add(Calendar.SECOND, 1)
            RepeatType.MINUTE -> calendar.add(Calendar.MINUTE, 1)
            RepeatType.HOUR -> calendar.add(Calendar.HOUR_OF_DAY, 1)
            RepeatType.DAY -> calendar.add(Calendar.DAY_OF_MONTH, 1)
            RepeatType.WEEK -> calendar.add(Calendar.WEEK_OF_MONTH, 1)
            RepeatType.MONTH -> calendar.add(Calendar.MONTH, 1)
            RepeatType.YEAR -> calendar.add(Calendar.YEAR, 1)
            RepeatType.CUSTOM -> days?.run { incrementToNextCustomAlarm(calendar, days) }
            RepeatType.NOT_REPEAT -> return
        }
    }

    private fun incrementToNextCustomAlarm(calendar: Calendar, days: String) {
        do {
            calendar.add(Calendar.DATE, 1)
            val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).toString()

        } while (days.contains(currentDayOfWeek).not())
    }

}