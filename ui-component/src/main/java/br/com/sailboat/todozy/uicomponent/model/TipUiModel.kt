package br.com.sailboat.todozy.uicomponent.model

data class TipUiModel(
    val text: String,
    override val index: Int = ViewType.TIP.ordinal
) : UiModel