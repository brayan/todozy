package br.com.sailboat.todozy.uicomponent.model

import java.util.*

data class TaskUiModel(
    val taskId: Long,
    val taskName: String,
    val alarm: Calendar? = null,
    val alarmColor: Int,
    override val uiModelId: Int = UiModelType.TASK.ordinal,
) : UiModel