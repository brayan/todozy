package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class GetTaskMetrics(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(filter: TaskHistoryFilter): TaskMetrics {
        val done = taskHistoryRepository.getTotalOfDoneTasks(filter)
        val notDone = taskHistoryRepository.getTotalOfNotDoneTasks(filter)
        val consecutiveDone = getTotalOfConsecutiveDoneTasks(filter)

        return TaskMetrics(doneTasks = done, notDoneTasks = notDone, consecutiveDone = consecutiveDone)
    }

    private suspend fun getTotalOfConsecutiveDoneTasks(filter: TaskHistoryFilter): Int {
        if (filter.taskId == Entity.NO_ID) {
            return 0
        }

        val history = taskHistoryRepository.getTaskHistory(filter.taskId)

        var consecutiveDone = 0

        if (history.isNotEmpty()) {

            for (h in history) {
                if (h.status == TaskStatus.DONE) consecutiveDone++ else break
            }
        }

        return consecutiveDone
    }

}