package br.com.sailboat.todozy.uicomponent.model

data class SubheadView(
    val subheadRes: Int,
    override val index: Int = ViewType.SUBHEADER.ordinal
) : UiModel