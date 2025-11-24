package br.com.sailboat.uicomponent.model

data class ImageTitleDividerUiModel(
    var imageRes: Int = 0,
    var title: String?,
    override val uiModelId: Int = UiModelType.IMAGE_TITLE.ordinal,
) : UiModel
