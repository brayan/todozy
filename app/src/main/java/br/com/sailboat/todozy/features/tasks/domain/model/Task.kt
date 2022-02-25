package br.com.sailboat.todozy.features.tasks.domain.model

import br.com.sailboat.todozy.utility.kotlin.model.Entity

data class Task(
    override var id: Long = NO_ID,
    val name: String,
    var notes: String?,
    var alarm: Alarm? = null,
) : Entity()