package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class GetTaskHistory(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(filter: TaskHistoryFilter): List<TaskHistory> = with(taskHistoryRepository) {
        return when (filter.category) {
            TaskHistoryCategory.TODAY -> getTodayHistory(filter)
            TaskHistoryCategory.YESTERDAY -> getYesterdayHistory(filter)
            TaskHistoryCategory.PREVIOUS_DAYS -> getPreviousDaysHistory(filter)
            else -> emptyList()
        }
    }

}