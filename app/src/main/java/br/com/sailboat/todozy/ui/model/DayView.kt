package br.com.sailboat.todozy.ui.model

data class DayView(val id: Int,
                   val name: String,
                   override val viewType: Int = ViewType.DAY.ordinal) : ItemView