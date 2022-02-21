package br.com.sailboat.todozy.core.presentation.model

data class ImageTitleDividerItemView(
    var imageRes: Int = 0,
    var title: String?,
    override val viewType: Int = ViewType.IMAGE_TITLE.ordinal
) : ItemView