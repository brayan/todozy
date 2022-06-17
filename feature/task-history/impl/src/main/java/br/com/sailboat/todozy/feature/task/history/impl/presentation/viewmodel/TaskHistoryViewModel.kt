package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.domain.service.CalendarService
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.UpdateHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetDateFilterNameViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetHistoryViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.orNewCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskHistoryViewModel(
    override val viewState: TaskHistoryViewState = TaskHistoryViewState(),
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getHistoryViewUseCase: GetHistoryViewUseCase,
    private val getDateFilterNameViewUseCase: GetDateFilterNameViewUseCase,
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase,
    private val taskHistoryUiModelToTaskHistoryMapper: TaskHistoryUiModelToTaskHistoryMapper,
    private val logService: LogService,
    private val calendarService: CalendarService,
) : BaseViewModel<TaskHistoryViewState, TaskHistoryViewAction>() {

    private var selectedItemPosition: Int = 0
    private var filter: TaskHistoryFilter = TaskHistoryFilter()
    private var dateFilterType = DateFilterTaskHistorySelectableItem.NO_FILTER
    private var taskStatusFilterType = TaskStatusSelectableItem.NO_FILTER

    override fun dispatchViewAction(viewAction: TaskHistoryViewAction) {
        when (viewAction) {
            is TaskHistoryViewAction.OnStart -> onStart()
            is TaskHistoryViewAction.OnClickFilter -> onClickFilter()
            is TaskHistoryViewAction.OnClickDateFilter -> onClickFilterDate()
            is TaskHistoryViewAction.OnClickStatusFilter -> onClickFilterStatus()
            is TaskHistoryViewAction.OnSelectDateFromFilter -> onSelectDateFromFilter(viewAction)
            is TaskHistoryViewAction.OnSelectStatusFromFilter -> onSelectStatusFromFilter(viewAction)
            is TaskHistoryViewAction.OnClickClearAllHistory -> onClickClearAllHistory()
            is TaskHistoryViewAction.OnClickDeleteTaskHistoryItem -> onClickDeleteTaskHistoryItem(viewAction)
            is TaskHistoryViewAction.OnClickConfirmDeleteTaskHistory -> onClickConfirmDeleteTaskHistory(viewAction)
            is TaskHistoryViewAction.OnClickConfirmClearAllHistory -> onClickConfirmClearAllHistory()
            is TaskHistoryViewAction.OnClickTaskHistory -> onClickTaskHistory(viewAction)
            is TaskHistoryViewAction.OnClickMarkTaskAsDone -> onClickMarkTaskAsDone(viewAction)
            is TaskHistoryViewAction.OnClickMarkTaskAsNotDone -> onClickMarkTaskAsNotDone(viewAction)
            is TaskHistoryViewAction.OnSelectDateRange -> onSelectDateRange(viewAction)
            is TaskHistoryViewAction.OnSubmitSearchTerm -> onSubmitSearch(viewAction)
        }
    }

    private fun onStart() {
        loadHistoryTasks()
    }

    private fun onClickFilter() {
        viewState.action.value = TaskHistoryViewState.Action.NavigateToMenuFilter(dateFilterType, taskStatusFilterType)
    }

    private fun onClickFilterDate() {
        viewState.action.value = TaskHistoryViewState.Action.NavigateToDateFilter(dateFilterType)
    }

    private fun onClickFilterStatus() {
        viewState.action.value = TaskHistoryViewState.Action.NavigateToStatusFilter(taskStatusFilterType)
    }

    private fun onSelectDateFromFilter(viewAction: TaskHistoryViewAction.OnSelectDateFromFilter) {
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

    private fun onSelectStatusFromFilter(viewAction: TaskHistoryViewAction.OnSelectStatusFromFilter) {
        if (viewAction.status === taskStatusFilterType) {
            return
        }
        taskStatusFilterType = viewAction.status

        when (taskStatusFilterType) {
            TaskStatusSelectableItem.NO_FILTER -> filter.status = null
            TaskStatusSelectableItem.TASKS_DONE -> filter.status = TaskStatus.DONE
            TaskStatusSelectableItem.TASKS_NOT_DONE -> filter.status = TaskStatus.NOT_DONE
        }

        loadHistoryTasks()
    }

    private fun onClickClearAllHistory() {
        viewState.action.value = TaskHistoryViewState.Action.NavigateToClearAllHistoryConfirmation
    }

    private fun onClickConfirmClearAllHistory() = viewModelScope.launch {
        try {
            clearHistorySelectedPosition()
            deleteAllHistoryUseCase()
            loadHistoryTasks()
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = TaskHistoryViewState.Action.ShowGenericError
        }
    }

    private fun onClickDeleteTaskHistoryItem(viewAction: TaskHistoryViewAction.OnClickDeleteTaskHistoryItem) {
        viewState.action.value =
            TaskHistoryViewState.Action.NavigateToDeleteTaskHistoryConfirmation(viewAction.position)
    }

    private fun onClickConfirmDeleteTaskHistory(viewAction: TaskHistoryViewAction.OnClickConfirmDeleteTaskHistory) {
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
                viewState.action.value = TaskHistoryViewState.Action.ShowGenericError
            }
        }
    }

    private fun onClickTaskHistory(viewAction: TaskHistoryViewAction.OnClickTaskHistory) {
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
                viewState.action.value = TaskHistoryViewState.Action.ScrollToPosition(viewAction.position)
            }
            else -> {
                selectedItemPosition = viewAction.position
                refreshHistoryItem(viewAction.position)
                viewState.action.value = TaskHistoryViewState.Action.ScrollToPosition(viewAction.position)
            }
        }
    }

    private fun onClickMarkTaskAsDone(viewAction: TaskHistoryViewAction.OnClickMarkTaskAsDone) {
        updateHistoryStatus(viewAction.position, TaskStatus.DONE)
    }

    private fun onClickMarkTaskAsNotDone(viewAction: TaskHistoryViewAction.OnClickMarkTaskAsNotDone) {
        updateHistoryStatus(viewAction.position, TaskStatus.NOT_DONE)
    }

    private fun onSelectDateRange(viewAction: TaskHistoryViewAction.OnSelectDateRange) {
        filter.initialDate = viewAction.initialDate
        filter.finalDate = viewAction.finalDate
        dateFilterType = DateFilterTaskHistorySelectableItem.DATE_RANGE

        setDateRangeSubtitle()
        loadHistoryTasks()
        viewState.action.value = TaskHistoryViewState.Action.ScrollToTop
    }

    private fun onSubmitSearch(viewAction: TaskHistoryViewAction.OnSubmitSearchTerm) {
        filter.text = viewAction.searchTerm
        loadHistoryTasks()
    }

    fun isShowingOptions(position: Int): Boolean {
        return selectedItemPosition == position
    }

    private fun onClickFilterNoFilter() {
        filter.initialDate = null
        filter.finalDate = null
        clearSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterToday() {
        filter.initialDate = Calendar.getInstance().apply { clearTime() }
        filter.finalDate = Calendar.getInstance().apply { setFinalTimeToCalendar() }

        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterYesterday() {
        filter.initialDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
            clearTime()
        }

        filter.finalDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
            setFinalTimeToCalendar()
        }

        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterLastSevenDays() {
        filter.initialDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -7)
            clearTime()
        }

        filter.finalDate = Calendar.getInstance().apply {
            setFinalTimeToCalendar()
        }

        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterLastThirtyDays() {
        filter.initialDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -30)
            clearTime()
        }

        filter.finalDate = Calendar.getInstance().apply {
            setFinalTimeToCalendar()
        }

        setDateFilterSubtitle()
        loadHistoryTasks()
    }

    private fun onClickFilterDateRange() {
        val initialDate = filter.initialDate.orNewCalendar()
        val finalDate = filter.finalDate.orNewCalendar()

        viewState.action.value = TaskHistoryViewState.Action.NavigateToDateRangeFilter(initialDate, finalDate)
    }

    private fun loadHistoryTasks() = viewModelScope.launch {
        try {
            val taskMetrics = async { getTaskMetricsUseCase(filter).getOrNull() }
            val taskHistoryList = async { getHistoryViewUseCase(filter).getOrThrow() }

            viewState.taskMetrics.postValue(taskMetrics.await())
            viewState.taskHistoryList.postValue(taskHistoryList.await())
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = TaskHistoryViewState.Action.ShowGenericError
        }
    }

    private fun clearSubtitle() {
        viewState.subtitle.value = ""
    }

    private fun setDateFilterSubtitle() {
        viewState.subtitle.value = getDateFilterNameViewUseCase(dateFilterType)
    }

    private fun setDateRangeSubtitle() = with(filter) {
        val initial = initialDate?.run { calendarService.getShortDate(this) }.orEmpty()
        val final = finalDate?.run { calendarService.getShortDate(this) }.orEmpty()
        viewState.subtitle.value = "$initial - $final"
    }

    private fun updateHistoryStatus(position: Int, status: TaskStatus) = viewModelScope.launch {
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
            viewState.action.value = TaskHistoryViewState.Action.ShowGenericError
        }
    }

    private fun clearHistorySelectedPosition() {
        selectedItemPosition = -1
    }

    private fun refreshHistoryItem(position: Int) {
        viewState.action.value = TaskHistoryViewState.Action.RefreshHistoryItem(position)
    }

    private fun hasHistorySelected(): Boolean {
        return selectedItemPosition != -1
    }
}
