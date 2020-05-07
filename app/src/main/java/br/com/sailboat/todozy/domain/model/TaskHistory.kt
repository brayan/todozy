package br.com.sailboat.todozy.domain.model

data class TaskHistory(
        val id: Long,
        val taskId: Long,
        val taskName: String,
        val status: TaskStatus,
        val insertingDate: String)