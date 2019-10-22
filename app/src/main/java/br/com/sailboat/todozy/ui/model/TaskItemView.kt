package br.com.sailboat.todozy.ui.model

import java.util.*

data class TaskItemView(var taskId: Long,
                        var taskName: String,
                        var alarm: Calendar?,
                        override val viewType: Int = ViewType.TASK.ordinal) : ItemView