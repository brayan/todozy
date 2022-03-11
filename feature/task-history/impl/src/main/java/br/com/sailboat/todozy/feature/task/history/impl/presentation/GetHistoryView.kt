package br.com.sailboat.todozy.feature.task.history.impl.presentation

import br.com.sailboat.todozy.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.R
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.GetTaskHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryToTaskHistoryUiModelMapper
import br.com.sailboat.todozy.uicomponent.model.SubheadUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

// TODO: Add unit tests
class GetHistoryView(
    private val getTasksHistoryUseCase: GetTaskHistoryUseCase,
    private val taskHistoryToTaskHistoryUiModelMapper: TaskHistoryToTaskHistoryUiModelMapper
) : GetHistoryViewUseCase {

    private val historyCategories = mapOf(
        TaskHistoryCategory.TODAY to R.string.today,
        TaskHistoryCategory.YESTERDAY to R.string.yesterday,
        TaskHistoryCategory.PREVIOUS_DAYS to R.string.previous_days,
    )

    override suspend operator fun invoke(filter: TaskHistoryFilter) = runCatching {
        coroutineScope {
            historyCategories.map { category ->
                async {
                    filter.category = category.key
                    getTaskHistoryView(filter.copyFilter(), category.value)
                }
            }.awaitAll().flatten()
        }
    }

    private suspend fun getTaskHistoryView(
        filter: TaskHistoryFilter,
        subhead: Int
    ): List<UiModel> {
        val history = getTasksHistoryUseCase(filter).getOrThrow()
        val historyView = mutableListOf<UiModel>()

        if (history.isNotEmpty()) {
            historyView.add(SubheadUiModel(subhead))
            historyView.addAll(history.map { taskHistoryToTaskHistoryUiModelMapper.map(it) })
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