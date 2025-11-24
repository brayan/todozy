package br.com.sailboat.uicomponent.model

data class TitleUiModel(
    val title: String,
    override val uiModelId: Int = UiModelType.TITLE.ordinal,
) : UiModel
