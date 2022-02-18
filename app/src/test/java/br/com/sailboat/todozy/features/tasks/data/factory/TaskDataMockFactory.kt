package br.com.sailboat.todozy.features.tasks.data.factory

import br.com.sailboat.todozy.features.tasks.data.model.TaskData

object TaskDataMockFactory {

    fun makeTaskData(
        id: Long = 45L,
        name: String = "Task Name",
        notes: String? = "Task Notes",
        insertingDate: String? = null, // TODO: ADD A VALID INSERTING DATE
        enabled: Boolean = true,
    ): TaskData {
        return TaskData(
            id = id,
            name = name,
            notes = notes,
            insertingDate = insertingDate,
            enabled = enabled,
        )
    }

    fun makeTaskDataList(task: TaskData = makeTaskData()) = listOf(task)
}