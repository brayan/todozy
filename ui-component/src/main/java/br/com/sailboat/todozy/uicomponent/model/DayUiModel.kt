package br.com.sailboat.todozy.uicomponent.model

data class DayUiModel(
    val id: Int,
    val name: String,
    override val index: Int = UiModelType.DAY.ordinal
) : UiModel