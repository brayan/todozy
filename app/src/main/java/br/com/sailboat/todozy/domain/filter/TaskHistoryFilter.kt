package br.com.sailboat.todozy.domain.filter

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import java.util.*

data class TaskHistoryFilter(var initialDate: Calendar? = null,
                             var finalDate: Calendar? = null,
                             var status: TaskStatusSelectableItem = TaskStatusSelectableItem.NO_FILTER,
                             var date: DateFilterTaskHistorySelectableItem = DateFilterTaskHistorySelectableItem.NO_FILTER,
                             var category: TaskHistoryCategory,
                             var taskId: Long = EntityHelper.NO_ID) : BaseFilter()

