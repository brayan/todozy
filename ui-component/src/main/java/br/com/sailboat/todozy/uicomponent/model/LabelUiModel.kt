package br.com.sailboat.todozy.uicomponent.model

data class LabelUiModel(
    val label: String,
    override val index: Int = ViewType.LABEL.ordinal
) : UiModel