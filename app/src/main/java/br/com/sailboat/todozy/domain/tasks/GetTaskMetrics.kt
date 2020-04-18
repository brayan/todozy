package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class GetTaskMetrics(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(taskId: Long): TaskMetrics {
        val filter = TaskHistoryFilter(taskId = taskId)

        val done = taskHistoryRepository.getTotalOfDoneTasks(filter)
        val notDone = taskHistoryRepository.getTotalOfNotDoneTasks(filter)
        val consecutiveDone = getTotalOfConsecutiveDoneTasks(filter)

        return TaskMetrics(doneTasks = done, notDoneTasks = notDone, consecutiveDone = consecutiveDone)
    }

    private suspend fun getTotalOfConsecutiveDoneTasks(filter: TaskHistoryFilter) : Int {
        if (filter.taskId == EntityHelper.NO_ID) {
            return 0
        }

        val history = taskHistoryRepository.getTaskHistory(filter.taskId)

        var consecutiveDone = 0

        if (history.isNotEmpty()) {

            for (h in history) {
                if (h.status == TaskStatus.DONE) {
                    consecutiveDone++
                } else {
                    break
                }
            }

        }

        return consecutiveDone
    }

}