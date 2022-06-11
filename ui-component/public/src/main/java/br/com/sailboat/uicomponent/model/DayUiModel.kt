package br.com.sailboat.uicomponent.model

data class DayUiModel(
    val id: Int,
    val name: String,
    override val uiModelId: Int = UiModelType.DAY.ordinal
) : UiModel