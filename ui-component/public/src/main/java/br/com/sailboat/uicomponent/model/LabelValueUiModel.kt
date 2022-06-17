package br.com.sailboat.uicomponent.model

data class LabelValueUiModel(
    val label: String,
    val value: String,
    override val uiModelId: Int = UiModelType.LABEL_VALUE.ordinal
) : UiModel
