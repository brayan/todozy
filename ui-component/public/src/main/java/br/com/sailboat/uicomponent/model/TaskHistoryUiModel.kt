package br.com.sailboat.uicomponent.model

data class TaskHistoryUiModel(
    val id: Long,
    val taskName: String,
    var done: Boolean,
    val insertingDate: String,
    override val uiModelId: Int = UiModelType.TASK_HISTORY.ordinal,
) : UiModel
