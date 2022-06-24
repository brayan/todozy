package br.com.sailboat.todozy.feature.alarm.impl.data.mapper

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.impl.data.model.AlarmData
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar

internal class AlarmDataToAlarmMapper {

    fun map(alarmData: AlarmData) =
        Alarm(
            // TODO: Refactor this
            dateTime = alarmData.nextAlarmDate!!.toDateTimeCalendar(),
            repeatType = RepeatType.indexOf(alarmData.repeatType),
            customDays = alarmData.days
        )
}
