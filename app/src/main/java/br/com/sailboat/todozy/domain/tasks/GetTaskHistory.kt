package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

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