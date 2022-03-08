package br.com.sailboat.todozy.uicomponent.model

data class SubheadUiModel(
    val subheadRes: Int,
    override val uiModelId: Int = UiModelType.SUBHEADER.ordinal
) : UiModel