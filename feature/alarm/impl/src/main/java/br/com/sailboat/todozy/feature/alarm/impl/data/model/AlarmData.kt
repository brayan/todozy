package br.com.sailboat.todozy.feature.alarm.impl.data.model

import br.com.sailboat.todozy.utility.kotlin.model.Entity

internal data class AlarmData(
    var id: Long = Entity.NO_ID,
    var taskId: Long = 0,
    var repeatType: Int = 0,
    var nextAlarmDate: String?,
    var insertingDate: String?,
    var days: String?,
)
