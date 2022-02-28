package br.com.sailboat.todozy.features.tasks.data.factory

import br.com.sailboat.todozy.feature.alarm.data.model.AlarmData
import br.com.sailboat.todozy.domain.model.RepeatType

object AlarmDataMockFactory {

    fun makeAlarmData(
        id: Long = 75L,
        taskId: Long = 45L,
        repeatType: Int = RepeatType.WEEK.ordinal,
        nextAlarmDate: String? = "2022-02-23 08:40:00",
        insertingDate: String? = "2022-02-23 08:29:52",
        days: String? = null,
    ) = AlarmData(
        id = id,
        taskId = taskId,
        repeatType = repeatType,
        nextAlarmDate = nextAlarmDate,
        insertingDate = insertingDate,
        days = days,
    )

    fun makeAlarmDataList(alarmData: AlarmData = makeAlarmData()) = listOf(alarmData)
}