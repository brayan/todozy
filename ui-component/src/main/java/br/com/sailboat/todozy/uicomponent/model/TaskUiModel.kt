package br.com.sailboat.todozy.uicomponent.model

import java.util.*

data class TaskUiModel(
    var taskId: Long,
    var taskName: String,
    var alarm: Calendar? = null,
    override val uiModelId: Int = UiModelType.TASK.ordinal,
) : UiModel