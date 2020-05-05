package br.com.sailboat.todozy.domain.filter

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import java.util.*

data class TaskHistoryFilter(var initialDate: Calendar? = null,
                             var finalDate: Calendar? = null,
                             var status: TaskStatus? = null,
                             var category: TaskHistoryCategory? = null,
                             var taskId: Long = EntityHelper.NO_ID) : BaseFilter()

