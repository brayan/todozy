package br.com.sailboat.todozy.uicomponent.model

data class SubheadUiModel(
    val subheadRes: Int,
    override val index: Int = UiModelType.SUBHEADER.ordinal
) : UiModel