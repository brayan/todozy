package br.com.sailboat.todozy.features.tasks.domain.model

import br.com.sailboat.todozy.core.base.Entity

data class TaskHistory(
    override var id: Long = NO_ID,
    val taskId: Long,
    val taskName: String,
    val status: TaskStatus,
    val insertingDate: String,
) : Entity()