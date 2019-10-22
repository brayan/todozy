package br.com.sailboat.todozy.ui.model

data class TipItemView(val text: String,
                       override val viewType: Int = ViewType.TIP.ordinal) : ItemView