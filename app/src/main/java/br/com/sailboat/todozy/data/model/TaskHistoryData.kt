package br.com.sailboat.todozy.data.model

import br.com.sailboat.todozy.domain.helper.EntityHelper

data class TaskHistoryData(var id: Long = EntityHelper.NO_ID,
                           var taskId: Long = 0,
                           var taskName: String?,
                           var status: Int = 0,
                           var insertingDate: String?,
                           var enabled: Boolean = true)