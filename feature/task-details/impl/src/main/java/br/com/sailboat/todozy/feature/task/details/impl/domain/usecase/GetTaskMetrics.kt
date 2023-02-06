package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class GetTaskMetrics(
    private val taskHistoryRepository: TaskHistoryRepository,
    private val taskRepository: TaskRepository,
) : GetTaskMetricsUseCase {

    override suspend operator fun invoke(filter: TaskHistoryFilter) = runCatching {
        coroutineScope {
            val done = async { taskHistoryRepository.getTotalOfDoneTasks(filter).getOrThrow() }
            val notDone = async {
                taskHistoryRepository.getTotalOfNotDoneTasks(filter).getOrThrow()
            }
            val consecutiveDone = async { getTotalOfConsecutiveDoneTasks(filter) }

            TaskMetrics(done.await(), notDone.await(), consecutiveDone.await().getOrThrow())
        }
    }

    private suspend fun getTotalOfConsecutiveDoneTasks(filter: TaskHistoryFilter): Result<Int> {
        return if (filter.taskId == Entity.NO_ID) {
            var cont = 0
            val tasks = taskRepository.getTasksWithAlarms().getOrThrow()
            repeat(tasks.size) { index ->
                cont += getTotalOfConsecutiveDoneTasksFromTaskId(tasks[index].id).getOrThrow()
            }
            Result.success(cont)
        } else {
            getTotalOfConsecutiveDoneTasksFromTaskId(filter.taskId)
        }
    }

    private suspend fun getTotalOfConsecutiveDoneTasksFromTaskId(taskId: Long): Result<Int> {
        val history = taskHistoryRepository.getTaskHistory(taskId).getOrThrow()

        var cont = 0

        history.forEach {
            if (it.status == TaskStatus.NOT_DONE) {
                return Result.success(cont)
            }
            cont++
        }
        return Result.success(cont)
    }
}
