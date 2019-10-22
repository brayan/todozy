package br.com.sailboat.todozy.ui.model

data class TitleItemView(val title: String,
                         override val viewType: Int = ViewType.TITLE.ordinal): ItemView