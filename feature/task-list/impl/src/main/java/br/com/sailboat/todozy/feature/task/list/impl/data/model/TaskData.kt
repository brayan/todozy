package br.com.sailboat.todozy.feature.task.list.impl.data.model

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.utility.kotlin.model.Entity

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