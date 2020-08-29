package br.com.sailboat.todozy.features.tasks.domain.model

import br.com.sailboat.todozy.core.base.Entity

data class Task(override var id: Long = NO_ID,
                val name: String,
                var notes: String?,
                var alarm: Alarm? = null) : Entity()