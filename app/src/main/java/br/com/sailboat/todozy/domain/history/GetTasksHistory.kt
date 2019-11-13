package br.com.sailboat.todozy.domain.history

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryCategory.*
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class GetTasksHistory(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(filter: TaskHistoryFilter): List<TaskHistory> = with(taskHistoryRepository) {
        return when (filter.category) {
            TODAY -> getTodayHistory(filter)
            YESTERDAY -> getYesterdayHistory(filter)
            PREVIOUS_DAYS -> getPreviousDaysHistory(filter)
        }
    }

}