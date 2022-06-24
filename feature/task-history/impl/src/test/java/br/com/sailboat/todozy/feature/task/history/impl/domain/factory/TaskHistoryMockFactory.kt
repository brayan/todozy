package br.com.sailboat.todozy.feature.task.history.impl.domain.factory

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory

internal object TaskHistoryMockFactory {

    fun makeTaskHistory(
        id: Long = 45L,
        taskId: Long = 55L,
        taskName: String = "Task Name",
        status: TaskStatus = TaskStatus.DONE,
        insertingDate: String = "",
    ) = TaskHistory(
        id = id,
        taskId = taskId,
        taskName = taskName,
        status = status,
        insertingDate = insertingDate,
    )

    fun makeTaskHistoryList(history: TaskHistory = makeTaskHistory()) = listOf(history)
}
