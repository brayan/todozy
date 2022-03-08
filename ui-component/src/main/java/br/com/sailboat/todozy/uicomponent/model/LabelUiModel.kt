package br.com.sailboat.todozy.uicomponent.model

data class LabelUiModel(
    val label: String,
    override val uiModelId: Int = UiModelType.LABEL.ordinal
) : UiModel