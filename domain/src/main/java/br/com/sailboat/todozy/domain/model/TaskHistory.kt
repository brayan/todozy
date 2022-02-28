package br.com.sailboat.todozy.domain.model

import br.com.sailboat.todozy.utility.kotlin.model.Entity

data class TaskHistory(
    override var id: Long = NO_ID,
    val taskId: Long,
    val taskName: String,
    val status: TaskStatus,
    val insertingDate: String,
) : Entity()