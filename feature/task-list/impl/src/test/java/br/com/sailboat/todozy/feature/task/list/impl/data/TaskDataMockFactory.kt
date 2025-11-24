package br.com.sailboat.todozy.feature.task.list.impl.data

import br.com.sailboat.todozy.feature.task.list.impl.data.model.TaskData
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import java.util.Calendar

internal object TaskDataMockFactory {
    fun makeTaskData(
        id: Long = 45L,
        name: String = "Task Name",
        notes: String? = "Task Notes",
        insertingDate: String? = Calendar.getInstance().toDateTimeString(),
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
