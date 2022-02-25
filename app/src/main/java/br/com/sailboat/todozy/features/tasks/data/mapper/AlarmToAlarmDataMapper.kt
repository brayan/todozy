package br.com.sailboat.todozy.features.tasks.data.mapper

import br.com.sailboat.todozy.features.tasks.data.model.AlarmData
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import br.com.sailboat.todozy.utility.kotlin.model.Entity

class AlarmToAlarmDataMapper {

    fun map(from: Alarm, taskId: Long) =
        AlarmData(
            id = Entity.NO_ID,
            taskId = taskId,
            repeatType = from.repeatType.ordinal,
            nextAlarmDate = from.dateTime.toDateTimeString(),
            insertingDate = null,
            days = from.customDays,
        )

}