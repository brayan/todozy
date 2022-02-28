package br.com.sailboat.todozy.uicomponent.model

data class EmptyListTaskUiModel(
    override val index: Int = UiModelType.EMPTY_LIST_TASK.ordinal
) : UiModel