package br.com.sailboat.uicomponent.model

data class SubheadUiModel(
    val subhead: String,
    override val uiModelId: Int = UiModelType.SUBHEADER.ordinal,
) : UiModel
