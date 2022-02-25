package br.com.sailboat.todozy.features.tasks.data.model

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString

data class AlarmData(
    var id: Long = Entity.NO_ID,
    var taskId: Long = 0,
    var repeatType: Int = 0,
    var nextAlarmDate: String?,
    var insertingDate: String?,
    var days: String?,
)

fun AlarmData.mapToAlarm() = Alarm(
    dateTime = nextAlarmDate!!.toDateTimeCalendar(),
    repeatType = RepeatType.indexOf(repeatType),
    customDays = days
)

fun Alarm.mapToAlarmData(taskId: Long) =
    AlarmData(
        id = Entity.NO_ID,
        taskId = taskId,
        repeatType = repeatType.ordinal,
        nextAlarmDate = dateTime.toDateTimeString(),
        insertingDate = null,
        days = customDays,
    )