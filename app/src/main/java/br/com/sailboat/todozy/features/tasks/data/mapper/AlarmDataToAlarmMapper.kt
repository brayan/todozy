package br.com.sailboat.todozy.features.tasks.data.mapper

import br.com.sailboat.todozy.features.tasks.data.model.AlarmData
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar

class AlarmDataToAlarmMapper {

    fun map(alarmData: AlarmData) =
        Alarm(
            // TODO: Refactor this
            dateTime = alarmData.nextAlarmDate!!.toDateTimeCalendar(),
            repeatType = RepeatType.indexOf(alarmData.repeatType),
            customDays = alarmData.days
        )

}