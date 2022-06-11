package br.com.sailboat.uicomponent.model

data class SubheadUiModel(
    val subheadRes: Int,
    override val uiModelId: Int = UiModelType.SUBHEADER.ordinal
) : UiModel