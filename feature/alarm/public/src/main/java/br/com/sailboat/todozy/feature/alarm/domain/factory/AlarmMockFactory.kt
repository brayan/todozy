package br.com.sailboat.todozy.feature.alarm.domain.factory

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import java.util.Calendar

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
