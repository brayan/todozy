package br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import java.util.*

class TaskCategoryToStringMapper(private val context: Context) {

    // TODO: MOVE TO STRING
    fun map(taskCategory: TaskCategory): Int {
        val category = when (taskCategory) {
            TaskCategory.BEFORE_TODAY -> R.string.previous_days
            TaskCategory.TODAY -> R.string.today
            TaskCategory.TOMORROW -> R.string.tomorrow
            TaskCategory.NEXT_DAYS -> R.string.next_days
            else -> R.string.empty
        }
//        return context.getString(category)
        return category
    }
}
