package br.com.sailboat.todozy.feature.task.history.impl.data.repository

import br.com.sailboat.todozy.feature.task.history.impl.data.model.TaskHistoryData
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import java.util.Calendar

internal object TaskHistoryDataMockFactory {

    fun makeTaskHistoryData(
        id: Long = 45L,
        taskId: Long = 45L,
        taskName: String = "Task Name",
        status: Int = 1,
        insertingDate: String? = Calendar.getInstance().toDateTimeString(),
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
