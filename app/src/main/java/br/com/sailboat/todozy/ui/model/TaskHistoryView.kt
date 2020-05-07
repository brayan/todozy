package br.com.sailboat.todozy.ui.model

data class TaskHistoryView(
        val id: Long,
        val taskName: String,
        var status : TaskStatusView,
        val insertingDate: String,
        override val viewType: Int = ViewType.TASK_HISTORY.ordinal): ItemView