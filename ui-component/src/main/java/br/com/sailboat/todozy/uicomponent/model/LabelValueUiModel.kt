package br.com.sailboat.todozy.uicomponent.model

data class LabelValueUiModel(
    val label: String,
    val value: String,
    override val index: Int = UiModelType.LABEL_VALUE.ordinal
) : UiModel