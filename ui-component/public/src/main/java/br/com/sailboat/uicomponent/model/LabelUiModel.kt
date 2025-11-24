package br.com.sailboat.uicomponent.model

data class LabelUiModel(
    val label: String,
    override val uiModelId: Int = UiModelType.LABEL.ordinal,
) : UiModel
