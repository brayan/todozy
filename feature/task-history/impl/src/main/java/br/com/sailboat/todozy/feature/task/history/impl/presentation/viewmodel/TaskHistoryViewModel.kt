package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.domain.service.CalendarService
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.GetTaskHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.UpdateHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetDateFilterNameViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.factory.TaskHistoryUiModelFactory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.orNewCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toEndOfDayCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toStartOfDayCalendar
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

internal class TaskHistoryViewModel(
    override val viewState: TaskHistoryViewState = TaskHistoryViewState(),
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase,
    private val getDateFilterNameViewUseCase: GetDateFilterNameViewUseCase,
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase,
    private val taskHistoryUiModelFactory: TaskHistoryUiModelFactory,
    private val taskHistoryUiModelToTaskHistoryMapper: TaskHistoryUiModelToTaskHistoryMapper,
    private val logService: LogService,
    private val calendarService: CalendarService,
) : BaseViewModel<TaskHistoryViewState, TaskHistoryViewIntent>() {
    private var selectedItemPosition: Int = 0
    private var filter: TaskHistoryFilter = TaskHistoryFilter()
    private var dateFilterType = DateFilterTaskHistorySelectableItem.NO_FILTER
    private var taskStatusFilterType = TaskStatusSelectableItem.NO_FILTER

    override fun dispatchViewIntent(viewIntent: TaskHistoryViewIntent) {
        when (viewIntent) {
            is TaskHistoryViewIntent.OnStart -> onStart()
            is TaskHistoryViewIntent.OnClickFilter -> onClickFilter()
            is TaskHistoryViewIntent.OnClickDateFilter -> onClickFilterDate()
            is TaskHistoryViewIntent.OnClickStatusFilter -> onClickFilterStatus()
            is TaskHistoryViewIntent.OnSelectDateFromFilter -> onSelectDateFromFilter(viewIntent)
            is TaskHistoryViewIntent.OnSelectStatusFromFilter -> onSelectStatusFromFilter(viewIntent)
            is TaskHistoryViewIntent.OnClickClearAllHistory -> onClickClearAllHistory()
            is TaskHistoryViewIntent.OnClickDeleteTaskHistoryItem -> onClickDeleteTaskHistoryItem(viewIntent)
            is TaskHistoryViewIntent.OnClickConfirmDeleteTaskHistory -> onClickConfirmDeleteTaskHistory(viewIntent)
            is TaskHistoryViewIntent.OnClickConfirmClearAllHistory -> onClickConfirmClearAllHistory()
            is TaskHistoryViewIntent.OnClickTaskHistory -> onClickTaskHistory(viewIntent)
            is TaskHistoryViewIntent.OnClickMarkTaskAsDone -> onClickMarkTaskAsDone(viewIntent)
            is TaskHistoryViewIntent.OnClickMarkTaskAsNotDone -> onClickMarkTaskAsNotDone(viewIntent)
            is TaskHistoryViewIntent.OnSelectDateRange -> onSelectDateRange(viewIntent)
            is TaskHistoryViewIntent.OnSubmitSearchTerm -> onSubmitSearch(viewIntent)
        }
    }

    private fun onStart() {
        loadHistoryTasks()
    }

    private fun onClickFilter() {
        viewState.viewAction.value = TaskHistoryViewAction.NavigateToMenuFilter(dateFilterType, taskStatusFilterType)
    }

    private fun onClickFilterDate() {
        viewState.viewAction.value = TaskHistoryViewAction.NavigateToDateFilter(dateFilterType)
    }

    private fun onClickFilterStatus() {
        viewState.viewAction.value = TaskHistoryViewAction.NavigateToStatusFilter(taskStatusFilterType)
    }

    private fun onSelectDateFromFilter(viewAction: TaskHistoryViewIntent.OnSelectDateFromFilter) {
        if (viewAction.date === dateFilterType &&
            viewAction.date !== DateFilterTaskHistorySelectableItem.DATE_RANGE
        ) {
            return
        }

        this.dateFilterType = viewAction.date

        when (viewAction.date) {
            DateFilterTaskHistorySelectableItem.NO_FILTER -> onClickFilterNoFilter()
            DateFilterTaskHistorySelectableItem.TODAY -> onClickFilterToday()
            DateFilterTaskHistorySelectableItem.YESTERDAY -> onClickFilterYesterday()
            DateFilterTaskHistorySelectableItem.LAST_7_DAYS -> onClickFilterLastSevenDays()
            DateFilterTaskHistorySelectableItem.LAST_30_DAYS -> onClickFilterLastThirtyDays()
            DateFilterTaskHistorySelectableItem.DATE_RANGE -> onClickFilterDateRange()
        }
    }

    private fun onSelectStatusFromFilter(viewAction: TaskHistoryViewIntent.OnSelectStatusFromFilter) {
        if (viewAction.status === taskStatusFilterType) {
            return
        }
        taskStatusFilterType = viewAction.status

        val status =
            when (taskStatusFilterType) {
                TaskStatusSelectableItem.NO_FILTER -> null
                TaskStatusSelectableItem.TASKS_DONE -> TaskStatus.DONE
                TaskStatusSelectableItem.TASKS_NOT_DONE -> TaskStatus.NOT_DONE
            }
        filter = filter.copy(status = status)

        loadHistoryTasks()
    }

    private fun onClickClearAllHistory() {
        viewState.viewAction.value = TaskHistoryViewAction.NavigateToClearAllHistoryConfirmation
    }

    private fun onClickConfirmClearAllHistory() =
        viewModelScope.launch {
            try {
                clearHistorySelectedPosition()
                deleteAllHistoryUseCase()
                loadHistoryTasks()
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskHistoryViewAction.ShowGenericError
            }
        }

    private fun onClickDeleteTaskHistoryItem(viewAction: TaskHistoryViewIntent.OnClickDeleteTaskHistoryItem) {
        viewState.viewAction.value = TaskHistoryViewAction.NavigateToDeleteTaskHistoryConfirmation(viewAction.position)
    }

    private fun onClickConfirmDeleteTaskHistory(viewAction: TaskHistoryViewIntent.OnClickConfirmDeleteTaskHistory) {
        viewModelScope.launch {
            try {
                clearHistorySelectedPosition()

                val taskHistoryList = viewState.taskHistoryList.value.orEmpty()
                val taskHistory = taskHistoryList[viewAction.position] as TaskHistoryUiModel

                val history = taskHistoryUiModelToTaskHistoryMapper.map(taskHistory)
                deleteHistoryUseCase(history).getOrThrow()
                loadHistoryTasks()
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskHistoryViewAction.ShowGenericError
            }
        }
    }

    private fun onClickTaskHistory(viewAction: TaskHistoryViewIntent.OnClickTaskHistory) {
        when {
            isShowingOptions(viewAction.position) -> {
                clearHistorySelectedPosition()
                refreshHistoryItem(viewAction.position)
            }
            hasHistorySelected() -> {
                val oldSelectedPosition = selectedItemPosition
                selectedItemPosition = viewAction.position
                refreshHistoryItem(oldSelectedPosition)
                refreshHistoryItem(viewAction.position)
                viewState.viewAction.value = TaskHistoryViewAction.ScrollToPosition(viewAction.position)
            }
            else -> {
                selectedItemPosition = viewAction.position
                refreshHistoryItem(viewAction.position)
                viewState.viewAction.value = TaskHistoryViewAction.ScrollToPosition(viewAction.position)
            }
        }
    }

    private fun onClickMarkTaskAsDone(viewAction: TaskHistoryViewIntent.OnClickMarkTaskAsDone) {
        updateHistoryStatus(viewAction.position, TaskStatus.DONE)
    }

    private fun onClickMarkTaskAsNotDone(viewAction: TaskHistoryViewIntent.OnClickMarkTaskAsNotDone) {
        updateHistoryStatus(viewAction.position, TaskStatus.NOT_DONE)
    }

    private fun onSelectDateRange(viewAction: TaskHistoryViewIntent.OnSelectDateRange) {
        filter =
            filter.copy(
                initialDate = viewAction.initialDate,
                finalDate = viewAction.finalDate,
            )
        dateFilterType = DateFilterTaskHistorySelectableItem.DATE_RANGE

        setDateRangeSubtitle()
        loadHistoryTasks()
        viewState.viewAction.value = TaskHistoryViewAction.ScrollToTop
    }

    private fun onSubmitSearch(viewAction: TaskHistoryViewIntent.OnSubmitSearchTerm) {
        filter = filter.copy(text = viewAction.searchTerm)
        loadHistoryTasks()
    }

    fun isShowingOptions(position: Int): Boolean {
        return selectedItemPosition == position
    }

    private fun onClickFilterNoFilter() {
        filter =
            filter.copy(
                initialDate = null,
                finalDate = null,
            )
        clearSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterToday() {
        filter =
            filter.copy(
                initialDate = Calendar.getInstance().apply { clearTime() },
                finalDate = Calendar.getInstance().apply { setFinalTimeToCalendar() },
            )
        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterYesterday() {
        val initialDate =
            Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -1)
                clearTime()
            }
        val finalDate =
            Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -1)
                setFinalTimeToCalendar()
            }
        filter =
            filter.copy(
                initialDate = initialDate,
                finalDate = finalDate,
            )

        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterLastSevenDays() {
        applyRangeFilter(TaskProgressRange.LAST_7_DAYS)
    }

    private fun onClickFilterLastThirtyDays() {
        applyRangeFilter(TaskProgressRange.LAST_30_DAYS)
    }

    private fun onClickFilterDateRange() {
        val initialDate = filter.initialDate.orNewCalendar()
        val finalDate = filter.finalDate.orNewCalendar()

        viewState.viewAction.value = TaskHistoryViewAction.NavigateToDateRangeFilter(initialDate, finalDate)
    }

    private fun applyRangeFilter(range: TaskProgressRange) {
        val today = LocalDate.now()
        filter =
            filter.copy(
                initialDate = range.startDate(today).toStartOfDayCalendar(),
                finalDate = today.toEndOfDayCalendar(),
            )

        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun loadHistoryTasks() =
        viewModelScope.launch {
            try {
                viewState.loading.postValue(true)

                val taskMetrics = async { getTaskMetricsUseCase(filter).getOrNull() }

                val taskHistoryCategories =
                    listOf(
                        TaskHistoryCategory.TODAY,
                        TaskHistoryCategory.YESTERDAY,
                        TaskHistoryCategory.PREVIOUS_DAYS,
                    )

                val taskHistoryList =
                    taskHistoryCategories.map { category ->
                        async {
                            val searchFilter =
                                filter.copy(
                                    text = filter.text,
                                    category = category,
                                )
                            val taskHistoryList = getTaskHistoryUseCase(searchFilter).getOrThrow()

                            taskHistoryUiModelFactory.create(taskHistoryList, category)
                        }
                    }.awaitAll().flatten()

                viewState.taskMetrics.postValue(taskMetrics.await())
                viewState.taskHistoryList.postValue(taskHistoryList)
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskHistoryViewAction.ShowGenericError
            } finally {
                viewState.loading.postValue(false)
            }
        }

    private fun clearSubtitle() {
        viewState.subtitle.value = ""
    }

    private fun setDateFilterSubtitle() {
        viewState.subtitle.value = getDateFilterNameViewUseCase(dateFilterType)
    }

    private fun setDateRangeSubtitle() =
        with(filter) {
            val initial = initialDate?.run { calendarService.getShortDate(this) }.orEmpty()
            val final = finalDate?.run { calendarService.getShortDate(this) }.orEmpty()
            viewState.subtitle.value = "$initial - $final"
        }

    private fun updateHistoryStatus(
        position: Int,
        status: TaskStatus,
    ) = viewModelScope.launch {
        try {
            clearHistorySelectedPosition()

            val historyView = viewState.taskHistoryList.value?.get(position) as TaskHistoryUiModel
            historyView.done = status == TaskStatus.DONE

            refreshHistoryItem(position)
            val history = taskHistoryUiModelToTaskHistoryMapper.map(historyView)
            updateHistoryUseCase(history).getOrThrow()

            val taskMetrics = getTaskMetricsUseCase(filter)
            viewState.taskMetrics.postValue(taskMetrics.getOrNull())
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskHistoryViewAction.ShowGenericError
        }
    }

    private fun clearHistorySelectedPosition() {
        selectedItemPosition = -1
    }

    private fun refreshHistoryItem(position: Int) {
        viewState.viewAction.value = TaskHistoryViewAction.RefreshHistoryItem(position)
    }

    private fun hasHistorySelected(): Boolean {
        return selectedItemPosition != -1
    }
}
