package br.com.sailboat.todozy.core.presentation.model

data class TitleItemView(
    val title: String,
    override val viewType: Int = ViewType.TITLE.ordinal
) : ItemView