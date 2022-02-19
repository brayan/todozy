package br.com.sailboat.todozy.features.tasks.data.model

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task

data class TaskData(
    var id: Long = Entity.NO_ID,
    var name: String?,
    var notes: String?,
    var insertingDate: String? = null,
    var enabled: Boolean = true,
)

fun TaskData.mapToTask(alarm: Alarm?) =
    Task(
        id = id,
        name = name.orEmpty(),
        notes = notes,
        alarm = alarm,
    )

fun Task.mapToTaskData() =
    TaskData(
        id = id,
        name = name,
        notes = notes,
    )