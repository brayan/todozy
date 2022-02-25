package br.com.sailboat.todozy.features.tasks.presentation.history

import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.model.mapToTaskHistoryView
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.GetTaskHistoryUseCase
import br.com.sailboat.todozy.uicomponent.model.SubheadView
import br.com.sailboat.todozy.uicomponent.model.UiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

// TODO: Add unit tests
class GetHistoryView(
    private val getTasksHistoryUseCase: GetTaskHistoryUseCase,
) : GetHistoryViewUseCase {

    private val historyCategories = mapOf(
        TaskHistoryCategory.TODAY to R.string.today,
        TaskHistoryCategory.YESTERDAY to R.string.yesterday,
        TaskHistoryCategory.PREVIOUS_DAYS to R.string.previous_days,
    )

    override suspend operator fun invoke(filter: TaskHistoryFilter) = coroutineScope {
        historyCategories.map { category ->
            async {
                filter.category = category.key
                getTaskHistoryView(filter.copyFilter(), category.value)
            }
        }.awaitAll().flatten()
    }

    private suspend fun getTaskHistoryView(
        filter: TaskHistoryFilter,
        subhead: Int
    ): List<UiModel> {
        val history = getTasksHistoryUseCase(filter)
        val historyView = mutableListOf<UiModel>()

        if (history.isNotEmpty()) {
            historyView.add(SubheadView(subhead))
            historyView.addAll(history.mapToTaskHistoryView())
        }

        return historyView
    }

    private fun TaskHistoryFilter.copyFilter() = TaskHistoryFilter(
        initialDate = initialDate,
        finalDate = finalDate,
        status = status,
        category = category,
        taskId = taskId
    ).apply { text = this@copyFilter.text }

}