package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetTaskMetrics(
    private val taskHistoryRepository: TaskHistoryRepository
) : GetTaskMetricsUseCase {

    override suspend operator fun invoke(filter: TaskHistoryFilter) = coroutineScope {
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

        var cont = 0

        history.forEach {
            if (it.status == TaskStatus.NOT_DONE) {
                return cont
            }
            cont++
        }

        return cont
    }

}