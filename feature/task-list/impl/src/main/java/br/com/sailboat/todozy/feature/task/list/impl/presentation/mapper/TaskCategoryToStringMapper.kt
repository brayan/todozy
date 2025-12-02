package br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import br.com.sailboat.uicomponent.impl.R as UiR

internal class TaskCategoryToStringMapper(private val stringProvider: StringProvider) {
    fun map(taskCategory: TaskCategory): String {
        val category =
            when (taskCategory) {
                TaskCategory.BEFORE_TODAY -> UiR.string.previous_days
                TaskCategory.BEFORE_NOW -> UiR.string.before_now
                TaskCategory.TODAY -> UiR.string.today
                TaskCategory.TOMORROW -> UiR.string.tomorrow
                TaskCategory.NEXT_DAYS -> UiR.string.next_days
                TaskCategory.WITH_ALARMS -> UiR.string.with_alarms
            }
        return stringProvider.getString(category)
    }
}
