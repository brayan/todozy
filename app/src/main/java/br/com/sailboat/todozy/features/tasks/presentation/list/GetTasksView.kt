package br.com.sailboat.todozy.features.tasks.presentation.list

import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.SubheadView
import br.com.sailboat.todozy.core.presentation.model.mapToTaskItemView
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

// TODO: Add unit tests
class GetTasksView(
    private val getTasksUseCase: GetTasksUseCase,
) : GetTasksViewUseCase {

    private val taskCategories = mapOf(
        TaskCategory.BEFORE_TODAY to R.string.previous_days,
        TaskCategory.TODAY to R.string.today,
        TaskCategory.TOMORROW to R.string.tomorrow,
        TaskCategory.NEXT_DAYS to R.string.next_days
    )

    override suspend operator fun invoke(search: String) = coroutineScope {
        taskCategories.map { taskType ->
            async {
                val filter = TaskFilter(taskType.key).apply { text = search }
                getTasksView(filter, taskType.value)
            }
        }.awaitAll().flatten()
    }

    private suspend fun getTasksView(filter: TaskFilter, subhead: Int): List<ItemView> {
        val tasks = getTasksUseCase(filter)
        val tasksView = mutableListOf<ItemView>()

        if (tasks.isNotEmpty()) {
            tasksView.add(SubheadView(subhead))
            tasksView.addAll(tasks.mapToTaskItemView())
        }

        return tasksView
    }

}