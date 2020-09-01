package br.com.sailboat.todozy.core.presentation.model

data class SubheadView(val subheadRes: Int,
                       override val viewType: Int = ViewType.SUBHEADER.ordinal) : ItemView