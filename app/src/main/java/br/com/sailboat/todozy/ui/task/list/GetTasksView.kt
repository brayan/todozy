package br.com.sailboat.todozy.ui.task.list

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.tasks.GetTasks
import br.com.sailboat.todozy.ui.mapper.mapToTaskItemView
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.SubheadView
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetTasksView(private val context: Context,
                   private val getTasks: GetTasks) {

    private val taskCategories = mapOf(
            TaskCategory.BEFORE_TODAY to R.string.previous_days,
            TaskCategory.TODAY to R.string.today,
            TaskCategory.TOMORROW to R.string.tomorrow,
            TaskCategory.NEXT_DAYS to R.string.next_days)

    suspend operator fun invoke(filter: TaskFilter) = coroutineScope {
        taskCategories.map { taskType ->
            async {
                filter.category = taskType.key
                getTasksView(filter, taskType.value)
            }
        }.awaitAll().flatten()
    }

    private suspend fun getTasksView(filter: TaskFilter, subhead: Int): List<ItemView> {
        val tasks = getTasks(filter)
        val tasksView = mutableListOf<ItemView>()

        if (tasks.isNotEmpty()) {
            tasksView.add(SubheadView(context.getString(subhead)))
            tasksView.addAll(tasks.mapToTaskItemView())
        }

        return tasksView
    }

}