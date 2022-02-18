package br.com.sailboat.todozy.features.tasks.domain.factory

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import java.util.*

object AlarmMockFactory {

    fun makeAlarm(
        dateTime: Calendar = Calendar.getInstance(),
        repeatType: RepeatType = RepeatType.WEEK,
        customDays: String? = null,
    ): Alarm {
        return Alarm(
            dateTime = dateTime,
            repeatType = repeatType,
            customDays = customDays
        )
    }

    fun makeAlarmList(alarm: Alarm = makeAlarm()) = listOf(alarm)
}