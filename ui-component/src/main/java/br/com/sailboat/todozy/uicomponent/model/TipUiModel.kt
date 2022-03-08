package br.com.sailboat.todozy.uicomponent.model

data class TipUiModel(
    val text: String,
    override val uiModelId: Int = UiModelType.TIP.ordinal
) : UiModel