package br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.feature.task.history.impl.R
import br.com.sailboat.todozy.utility.kotlin.StringProvider

internal class TaskHistoryCategoryToStringMapper(private val stringProvider: StringProvider) {

    fun map(taskHistoryCategory: TaskHistoryCategory): String {
        val category = when (taskHistoryCategory) {
            TaskHistoryCategory.TODAY -> R.string.today
            TaskHistoryCategory.YESTERDAY -> R.string.yesterday
            TaskHistoryCategory.PREVIOUS_DAYS -> R.string.previous_days
        }
        return stringProvider.getString(category)
    }
}
