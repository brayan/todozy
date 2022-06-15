package br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.todozy.utility.android.string.StringProvider

class TaskCategoryToStringMapper(private val stringProvider: StringProvider) {

    fun map(taskCategory: TaskCategory): String {
        val category = when (taskCategory) {
            TaskCategory.BEFORE_TODAY -> R.string.previous_days
            TaskCategory.TODAY -> R.string.today
            TaskCategory.TOMORROW -> R.string.tomorrow
            TaskCategory.NEXT_DAYS -> R.string.next_days
            else -> R.string.empty
        }
        return stringProvider.getString(category)
    }
}
