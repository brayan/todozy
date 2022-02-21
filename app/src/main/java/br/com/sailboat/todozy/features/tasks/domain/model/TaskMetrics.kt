package br.com.sailboat.todozy.features.tasks.domain.model

data class TaskMetrics(
    val doneTasks: Int,
    val notDoneTasks: Int,
    val consecutiveDone: Int
)

