package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository

class GetTaskHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : GetTaskHistoryUseCase {

    override suspend operator fun invoke(filter: TaskHistoryFilter): List<TaskHistory> =
        with(taskHistoryRepository) {
            return when (filter.category) {
                TaskHistoryCategory.TODAY -> getTodayHistory(filter)
                TaskHistoryCategory.YESTERDAY -> getYesterdayHistory(filter)
                TaskHistoryCategory.PREVIOUS_DAYS -> getPreviousDaysHistory(filter)
                else -> emptyList()
            }
        }

}