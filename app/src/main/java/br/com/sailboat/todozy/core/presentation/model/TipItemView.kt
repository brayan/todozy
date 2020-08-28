package br.com.sailboat.todozy.core.presentation.model

data class TipItemView(val text: String,
                       override val viewType: Int = ViewType.TIP.ordinal) : ItemView