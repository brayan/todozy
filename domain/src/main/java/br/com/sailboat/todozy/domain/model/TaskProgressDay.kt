package br.com.sailboat.todozy.domain.model

import java.time.LocalDate

data class TaskProgressDay(
    val date: LocalDate,
    val doneCount: Int,
    val notDoneCount: Int,
    val totalCount: Int,
)
