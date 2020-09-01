package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetTaskMetrics(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(filter: TaskHistoryFilter) = coroutineScope {
        val done = async { taskHistoryRepository.getTotalOfDoneTasks(filter) }
        val notDone = async { taskHistoryRepository.getTotalOfNotDoneTasks(filter) }
        val consecutiveDone = async { getTotalOfConsecutiveDoneTasks(filter) }

        TaskMetrics(done.await(), notDone.await(), consecutiveDone.await())
    }

    private suspend fun getTotalOfConsecutiveDoneTasks(filter: TaskHistoryFilter): Int {
        if (filter.taskId == Entity.NO_ID) {
            return 0
        }

        val history = taskHistoryRepository.getTaskHistory(filter.taskId)
        return history.count { it.status == TaskStatus.DONE }
    }

}