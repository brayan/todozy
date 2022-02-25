package br.com.sailboat.todozy.uicomponent.model

data class ImageTitleDividerUiModel(
    var imageRes: Int = 0,
    var title: String?,
    override val index: Int = ViewType.IMAGE_TITLE.ordinal
) : UiModel