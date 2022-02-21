package br.com.sailboat.todozy.core.presentation.model

data class LabelItemView(
    val label: String,
    override val viewType: Int = ViewType.LABEL.ordinal
) : ItemView