package br.com.sailboat.todozy.features.tasks.domain.model

data class Task(var id: Long,
                val name: String,
                var notes: String?,
                var alarm: Alarm? = null)