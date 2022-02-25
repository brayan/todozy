package br.com.sailboat.todozy.uicomponent.model

data class TitleUiModel(
    val title: String,
    override val index: Int = UiModelType.TITLE.ordinal
) : UiModel