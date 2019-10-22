package br.com.sailboat.todozy.domain.model

data class Task(var id: Long,
        val name: String,
        var notes: String?,
        var alarm: Alarm? = null)