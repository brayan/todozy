package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.GregorianCalendar

internal class GetTaskMetricsUseCaseImpl(
    private val taskHistoryRepository: TaskHistoryRepository,
) : GetTaskMetricsUseCase {
    override suspend operator fun invoke(filter: TaskHistoryFilter) = runCatching {
        coroutineScope {
            val done = async { taskHistoryRepository.getTotalOfDoneTasks(filter).getOrThrow() }
            val notDone =
                async {
                    taskHistoryRepository.getTotalOfNotDoneTasks(filter).getOrThrow()
                }
            val consecutiveDone = async { getTotalOfConsecutiveDoneTasks(filter) }

            TaskMetrics(done.await(), notDone.await(), consecutiveDone.await().getOrThrow())
        }
    }

    private suspend fun getTotalOfConsecutiveDoneTasks(filter: TaskHistoryFilter): Result<Int> = runCatching {
        if (filter.taskId == Entity.NO_ID) {
            return@runCatching 0
        }

        val history =
            taskHistoryRepository
                .getHistory(filter)
                .getOrThrow()
                .sortedByDescending { it.insertingDateMillis() }

        var cont = 0

        history.forEach {
            if (it.status == TaskStatus.NOT_DONE) {
                return@runCatching cont
            }
            cont++
        }

        return@runCatching cont
    }

    private fun TaskHistory.insertingDateMillis(): Long = runCatching { insertingDate.toDateTimeCalendar().timeInMillis }
        .getOrElse {
            runCatching {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
                val localDateTime = LocalDateTime.parse(insertingDate, formatter)
                GregorianCalendar.from(localDateTime.atZone(ZoneId.systemDefault())).timeInMillis
            }.getOrElse { 0L }
        }
}
