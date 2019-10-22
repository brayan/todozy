package br.com.sailboat.todozy.ui.model

data class LabelValueItemView(val label: String,
                              val value: String,
                              override val viewType: Int = ViewType.LABEL_VALUE.ordinal) : ItemView