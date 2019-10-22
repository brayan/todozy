package br.com.sailboat.todozy.domain.model

data class TaskMetrics(val doneTasks: Int,
                       val notDoneTasks: Int,
                       val consecutiveDone: Int)

