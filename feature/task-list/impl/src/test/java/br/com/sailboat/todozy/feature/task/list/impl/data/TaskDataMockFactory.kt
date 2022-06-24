package br.com.sailboat.todozy.feature.task.list.impl.data

import br.com.sailboat.todozy.feature.task.list.impl.data.model.TaskData

internal object TaskDataMockFactory {

    fun makeTaskData(
        id: Long = 45L,
        name: String = "Task Name",
        notes: String? = "Task Notes",
        insertingDate: String? = null, // TODO: ADD A VALID INSERTING DATE
        enabled: Boolean = true,
    ) = TaskData(
        id = id,
        name = name,
        notes = notes,
        insertingDate = insertingDate,
        enabled = enabled,
    )

    fun makeTaskDataList(task: TaskData = makeTaskData()) = listOf(task)
}
