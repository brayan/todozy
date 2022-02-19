package br.com.sailboat.todozy.features.tasks.data.factory

import br.com.sailboat.todozy.features.tasks.data.model.TaskHistoryData

object TaskHistoryDataMockFactory {

    fun makeTaskHistoryData(
        id: Long = 45L,
        taskId: Long = 45L,
        taskName: String = "Task Name",
        status: Int = 1,
        insertingDate: String? = null, // TODO: ADD A VALID INSERTING DATE
        enabled: Boolean = true,
    ) = TaskHistoryData(
        id = id,
        taskId = taskId,
        taskName = taskName,
        status = status,
        insertingDate = insertingDate,
        enabled = enabled,
    )

    fun makeTaskHistoryDataList(history: TaskHistoryData = makeTaskHistoryData()) = listOf(history)
}