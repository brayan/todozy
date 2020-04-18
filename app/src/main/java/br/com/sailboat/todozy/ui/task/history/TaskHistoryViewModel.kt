package br.com.sailboat.todozy.ui.task.history

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.ui.model.ItemView
import java.util.ArrayList

class TaskHistoryViewModel {

    var taskId = EntityHelper.NO_ID
    val history = mutableListOf<ItemView>()
    var selectedItemPosition: Int = 0
    var filter: TaskHistoryFilter = TaskHistoryFilter()
    var taskMetrics: TaskMetrics? = null
}