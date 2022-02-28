package br.com.sailboat.todozy.domain.model

import br.com.sailboat.todozy.utility.kotlin.model.BaseFilter
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import java.util.*

data class TaskHistoryFilter(
    var initialDate: Calendar? = null,
    var finalDate: Calendar? = null,
    var status: TaskStatus? = null,
    var category: TaskHistoryCategory? = null,
    var taskId: Long = Entity.NO_ID,
) : BaseFilter()

