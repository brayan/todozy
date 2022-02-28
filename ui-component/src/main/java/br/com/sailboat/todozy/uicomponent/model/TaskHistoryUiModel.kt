package br.com.sailboat.todozy.uicomponent.model

data class TaskHistoryUiModel(
    val id: Long,
    val taskName: String,
    var done: Boolean,
    val insertingDate: String,
    override val index: Int = UiModelType.TASK_HISTORY.ordinal
) : UiModel