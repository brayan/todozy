package br.com.sailboat.todozy.ui.model

data class TaskHistoryView(
        val taskName: String,
        val status : TaskStatusView,
        val insertingDate: String)