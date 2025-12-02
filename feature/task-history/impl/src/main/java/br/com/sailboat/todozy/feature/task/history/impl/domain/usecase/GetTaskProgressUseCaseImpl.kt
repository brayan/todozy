package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.task.history.domain.usecase.GetTaskProgressUseCase
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toEndOfDayCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toStartOfDayCalendar
import java.time.Clock
import java.time.LocalDate

internal class GetTaskProgressUseCaseImpl(
    private val taskHistoryRepository: TaskHistoryRepository,
    private val clock: Clock = Clock.systemDefaultZone(),
) : GetTaskProgressUseCase {
    override suspend fun invoke(filter: TaskProgressFilter): Result<List<TaskProgressDay>> = runCatching {
        val today = LocalDate.now(clock)
        val historyFilter = buildHistoryFilter(filter, today)
        val history = taskHistoryRepository.getHistory(historyFilter).getOrThrow()
        val historyByDate =
            history.groupBy { taskHistory ->
                taskHistory.insertingDate
                    .toDateTimeCalendar()
                    .toInstant()
                    .atZone(clock.zone)
                    .toLocalDate()
            }

        val startDate =
            when (filter.range) {
                TaskProgressRange.ALL -> historyByDate.keys.minOrNull() ?: today
                else -> filter.range.startDate(today)
            }
        val dateRange = buildDateRange(startDate, today)

        return@runCatching dateRange.map { date ->
            val entries = historyByDate[date].orEmpty()
            val doneCount = entries.count { it.status == TaskStatus.DONE }
            val notDoneCount = entries.count { it.status == TaskStatus.NOT_DONE }

            TaskProgressDay(
                date = date,
                doneCount = doneCount,
                notDoneCount = notDoneCount,
                totalCount = entries.size,
            )
        }
    }

    private fun buildHistoryFilter(
        filter: TaskProgressFilter,
        today: LocalDate,
    ): TaskHistoryFilter {
        if (filter.range == TaskProgressRange.ALL) {
            return TaskHistoryFilter(
                text = filter.text,
                taskId = filter.taskId,
            )
        }

        val startDate = filter.range.startDate(today)

        return TaskHistoryFilter(
            text = filter.text,
            initialDate = startDate.toStartOfDayCalendar(clock.zone),
            finalDate = today.toEndOfDayCalendar(clock.zone),
            taskId = filter.taskId,
        )
    }

    private fun buildDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<LocalDate> = generateSequence(startDate) { currentDate ->
        currentDate.plusDays(1)
    }.takeWhile { currentDate ->
        currentDate.isAfter(endDate).not()
    }.toList()
}
