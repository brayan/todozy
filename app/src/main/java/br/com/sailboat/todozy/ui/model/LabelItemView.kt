package br.com.sailboat.todozy.ui.model

data class LabelItemView(val label: String,
                         override val viewType: Int = ViewType.LABEL.ordinal) : ItemView