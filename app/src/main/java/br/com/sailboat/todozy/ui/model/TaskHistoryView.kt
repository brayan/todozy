package br.com.sailboat.todozy.ui.model

data class TaskHistoryView(
        val taskName: String,
        val status : TaskStatusView,
        val insertingDate: String,
        override val viewType: Int = ViewType.TASK_HISTORY.ordinal): ItemView