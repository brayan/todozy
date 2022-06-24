package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository

internal class GetTaskHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : GetTaskHistoryUseCase {

    override suspend operator fun invoke(filter: TaskHistoryFilter): Result<List<TaskHistory>> {
        with(taskHistoryRepository) {
            return when (filter.category) {
                TaskHistoryCategory.TODAY -> getTodayHistory(filter)
                TaskHistoryCategory.YESTERDAY -> getYesterdayHistory(filter)
                TaskHistoryCategory.PREVIOUS_DAYS -> getPreviousDaysHistory(filter)
                else -> Result.success(emptyList())
            }
        }
    }
}
