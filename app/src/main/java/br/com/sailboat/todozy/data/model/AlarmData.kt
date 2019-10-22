package br.com.sailboat.todozy.data.model

import br.com.sailboat.todozy.domain.helper.EntityHelper

data class AlarmData(var id: Long = EntityHelper.NO_ID,
                     var taskId: Long = 0,
                     var repeatType: Int = 0,
                     var nextAlarmDate: String?,
                     var insertingDate: String?,
                     var days: String?)