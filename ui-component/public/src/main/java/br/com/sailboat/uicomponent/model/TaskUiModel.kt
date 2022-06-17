package br.com.sailboat.uicomponent.model

import java.util.Calendar

data class TaskUiModel(
    val taskId: Long,
    val taskName: String,
    val alarm: Calendar? = null,
    val alarmColor: Int? = null,
    override val uiModelId: Int = UiModelType.TASK.ordinal,
) : UiModel
