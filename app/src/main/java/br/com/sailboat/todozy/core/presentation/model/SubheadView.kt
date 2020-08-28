package br.com.sailboat.todozy.core.presentation.model

data class SubheadView(val text: String,
                       override val viewType: Int = ViewType.SUBHEADER.ordinal) : ItemView