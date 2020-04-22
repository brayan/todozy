package br.com.sailboat.todozy.data.repository.mock

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class TaskHistoryRepositoryMock : TaskHistoryRepository {

    override suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter) = 5

    override suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter) = 10

    override suspend fun getTaskHistory(taskId: Long): List<TaskHistory> {
        return emptyList()
    }

    override suspend fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        val history = mutableListOf<TaskHistory>()
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-16 20:18:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-16 20:17:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-16 20:16:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-16 20:15:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-16 20:14:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-16 20:13:30"))

        return history
    }

    override suspend fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        val history = mutableListOf<TaskHistory>()
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:18:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:17:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:16:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:15:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:14:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:13:30"))

        return history
    }

    override suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        val history = mutableListOf<TaskHistory>()
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:18:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:17:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:16:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:15:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:14:30"))
        history.add(TaskHistory(0, "Task", TaskStatus.DONE, "2020-04-15 20:13:30"))

        return history
    }

    override suspend fun insert(task: Task, status: TaskStatus) {

    }

}