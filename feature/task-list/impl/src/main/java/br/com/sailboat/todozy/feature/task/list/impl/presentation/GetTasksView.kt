package br.com.sailboat.todozy.feature.task.list.impl.presentation

import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.uicomponent.model.SubheadUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

// TODO: Add unit tests
class GetTasksView(
    private val getTasksUseCase: GetTasksUseCase,
    private val taskToTaskUiModelMapper: TaskToTaskUiModelMapper,
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

    private suspend fun getTasksView(filter: TaskFilter, subhead: Int): List<UiModel> {
        val tasks = getTasksUseCase(filter)
        val tasksView = mutableListOf<UiModel>()

        if (tasks.isNotEmpty()) {
            tasksView.add(SubheadUiModel(subhead))
            tasksView.addAll(tasks.map { taskToTaskUiModelMapper.map(it) })
        }

        return tasksView
    }

}