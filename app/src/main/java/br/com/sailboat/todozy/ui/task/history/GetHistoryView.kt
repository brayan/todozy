package br.com.sailboat.todozy.ui.task.history

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.exceptions.FilterException
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.helper.isBeforeYesterday
import br.com.sailboat.todozy.domain.helper.isToday
import br.com.sailboat.todozy.domain.helper.isYesterday
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.domain.tasks.GetTaskHistory
import br.com.sailboat.todozy.ui.mapper.mapToTaskHistoryView
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.SubheadView
import br.com.sailboat.todozy.ui.model.ViewType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

class GetHistoryView(private val context: Context,
                     private val getTasksHistory: GetTaskHistory) {

    private val historyCategories = mapOf(
            TaskHistoryCategory.TODAY to R.string.today,
            TaskHistoryCategory.YESTERDAY to R.string.yesterday,
            TaskHistoryCategory.PREVIOUS_DAYS to R.string.previous_days)

    suspend operator fun invoke(filter: TaskHistoryFilter) = coroutineScope {
        historyCategories.map { taskType ->
            async {
                filter.category = taskType.key
                getTaskHistoryView(filter, taskType.value)
            }
        }.awaitAll().flatten()
    }

    private suspend fun getTaskHistoryView(filter: TaskHistoryFilter, subhead: Int): List<ItemView> {
        val history = getTasksHistory(filter)
        val historyView = mutableListOf<ItemView>()

        if (history.isNotEmpty()) {
            historyView.add(SubheadView(context.getString(subhead)))
            historyView.addAll(history.mapToTaskHistoryView())
        }

        return historyView
    }

}