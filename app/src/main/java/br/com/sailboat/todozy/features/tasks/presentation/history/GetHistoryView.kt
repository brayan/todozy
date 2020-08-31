package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.SubheadView
import br.com.sailboat.todozy.core.presentation.model.mapToTaskHistoryView
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.GetTaskHistory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetHistoryView(private val context: Context,
                     private val getTasksHistory: GetTaskHistory) {

    private val historyCategories = mapOf(
            TaskHistoryCategory.TODAY to R.string.today,
            TaskHistoryCategory.YESTERDAY to R.string.yesterday,
            TaskHistoryCategory.PREVIOUS_DAYS to R.string.previous_days)

    suspend operator fun invoke(filter: TaskHistoryFilter) = coroutineScope {
        historyCategories.map { category ->
            async {
                filter.category = category.key
                getTaskHistoryView(filter, category.value)
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