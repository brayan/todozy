package br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import br.com.sailboat.uicomponent.impl.R as UiR

internal class TaskHistoryCategoryToStringMapper(private val stringProvider: StringProvider) {
    fun map(taskHistoryCategory: TaskHistoryCategory): String {
        val category =
            when (taskHistoryCategory) {
                TaskHistoryCategory.TODAY -> UiR.string.today
                TaskHistoryCategory.YESTERDAY -> UiR.string.yesterday
                TaskHistoryCategory.PREVIOUS_DAYS -> UiR.string.previous_days
            }
        return stringProvider.getString(category)
    }
}
