package br.com.sailboat.uicomponent.model

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import java.util.Calendar

data class TaskUiModel(
    val taskId: Long,
    val taskName: String,
    val alarm: Calendar? = null,
    val alarmColor: Int? = null,
    val showInlineMetrics: Boolean = false,
    val inlineMetrics: TaskMetrics? = null,
    val inlineStatus: TaskStatus? = null,
    override val uiModelId: Int = UiModelType.TASK.ordinal,
) : UiModel
