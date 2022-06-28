package br.com.sailboat.todozy.feature.task.history.domain.model

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.todozy.utility.kotlin.model.Filter
import java.util.Calendar

data class TaskHistoryFilter(
    override val text: String? = null,
    val initialDate: Calendar? = null,
    val finalDate: Calendar? = null,
    val status: TaskStatus? = null,
    val category: TaskHistoryCategory? = null,
    val taskId: Long = Entity.NO_ID,
) : Filter
