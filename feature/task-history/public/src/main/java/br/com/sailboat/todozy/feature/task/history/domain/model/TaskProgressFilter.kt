package br.com.sailboat.todozy.feature.task.history.domain.model

import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.utility.kotlin.model.Entity

data class TaskProgressFilter(
    val range: TaskProgressRange,
    val taskId: Long = Entity.NO_ID,
)
