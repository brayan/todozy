package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.UpdateHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetDateFilterNameViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetHistoryViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetShortDateViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.todozy.uicomponent.helper.Event
import br.com.sailboat.todozy.uicomponent.model.TaskHistoryUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TaskHistoryViewModel(

    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getHistoryViewUseCase: GetHistoryViewUseCase,
    private val getShortDateViewUseCase: GetShortDateViewUseCase,
    private val getDateFilterNameViewUseCase: GetDateFilterNameViewUseCase,
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase,
    private val taskHistoryUiModelToTaskHistoryMapper: TaskHistoryUiModelToTaskHistoryMapper,
) : BaseViewModel<TaskHistoryViewState, TaskHistoryViewAction>() {

    override val viewState = TaskHistoryViewState()

    private var taskId = Entity.NO_ID
    val history = mutableListOf<UiModel>()
    private var selectedItemPosition: Int = 0
    private var filter: TaskHistoryFilter = TaskHistoryFilter()
    private var dateRangeType = DateFilterTaskHistorySelectableItem.NO_FILTER
    private var statusSelectableItem = TaskStatusSelectableItem.NO_FILTER

    val logError = Event<Exception>()
    val refreshHistory = Event<Unit>()
    val removeHistoryItem = Event<Int>()
    val showFilter = Event<Unit>()
    val showConfirmationClearAllHistory = Event<Unit>()
    val showConfirmationDelete = Event<Int>()
    val subtitle = MutableLiveData<String>()
    val doneTasks = MutableLiveData<Int>()
    val notDoneTasks = MutableLiveData<Int>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val updateHistoryItem = Event<Int>()
    val scrollTo = Event<Int>()

    fun start() {
        loadHistoryTasks()
    }

    fun onClickMenuFilter() {
        showFilter.value = Unit
    }

    fun onClickCleanAllHistory() {
        showConfirmationClearAllHistory.value = Unit
    }

    fun onClickMarkTaskAsDone(position: Int) {
        updateHistoryStatus(position, TaskStatus.DONE)
    }

    fun onClickMarkTaskAsNotDone(position: Int) {
        updateHistoryStatus(position, TaskStatus.NOT_DONE)
    }

    fun onClickHistory(position: Int) {
        when {
            isShowingOptions(position) -> {
                clearHistorySelectedPosition()
                updateHistoryItem(position)
            }
            hasHistorySelected() -> {
                val oldSelectedPosition = selectedItemPosition
                selectedItemPosition = position
                updateHistoryItem(oldSelectedPosition)
                updateHistoryItem(position)
                scrollTo(position)
            }
            else -> {
                selectedItemPosition = position
                updateHistoryItem(position)
                scrollTo(position)
            }
        }
    }

    fun isShowingOptions(position: Int): Boolean {
        return selectedItemPosition == position
    }

    fun onClickDelete(position: Int) {
        showConfirmationDelete.value = position
    }

    fun onSubmitSearch(search: String) {
        filter.text = search
        loadHistoryTasks()
    }

    fun onClickFilterDate() {

    }

    fun onClickFilterDate(dateRangeTypeSelected: DateFilterTaskHistorySelectableItem) {
        if (dateRangeTypeSelected === dateRangeType &&
            dateRangeTypeSelected !== DateFilterTaskHistorySelectableItem.DATE_RANGE
        ) {
            return
        }

        dateRangeType = dateRangeTypeSelected

        when (dateRangeTypeSelected) {
            DateFilterTaskHistorySelectableItem.NO_FILTER -> onClickFilterNoFilter()
            DateFilterTaskHistorySelectableItem.TODAY -> onClickFilterToday()
            DateFilterTaskHistorySelectableItem.YESTERDAY -> onClickFilterYesterday()
            DateFilterTaskHistorySelectableItem.LAST_7_DAYS -> onClickFilterLastSevenDays()
            DateFilterTaskHistorySelectableItem.LAST_30_DAYS -> onClickFilterLastThirtyDays()
            DateFilterTaskHistorySelectableItem.DATE_RANGE -> onClickFilterDateRange()
        }
    }

    private fun onClickFilterDateRange() {
//        val initial = filter.initialDate ?: Calendar.getInstance()
//        val final = filter.finalDate ?: Calendar.getInstance()
//
//        view?.showDateRangeSelectorDialog(initial, final)
    }

    private fun onClickFilterLastThirtyDays() {
        filter.initialDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -30)
            clearTime()
        }

        filter.finalDate = Calendar.getInstance().apply {
            setFinalTimeToCalendar()
        }

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

        loadHistoryTasks()
    }

    private fun onClickFilterToday() {
        filter.initialDate = Calendar.getInstance().apply { clearTime() }
        filter.finalDate = Calendar.getInstance().apply { setFinalTimeToCalendar() }

        loadHistoryTasks()
    }

    private fun onClickFilterNoFilter() {
        filter.initialDate = null
        filter.finalDate = null
        loadHistoryTasks()
    }

    fun onClickFilterStatus() {
        TODO("Not yet implemented")
    }

    fun onClickOkDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        TODO("Not yet implemented")
    }

    fun onClickFilterStatusItem(taskStatusSelectableItem: TaskStatusSelectableItem) {
        TODO("Not yet implemented")
    }

    fun onClickYesClearAllHistory() {
        TODO("Not yet implemented")
    }

    fun onClickYesDeleteHistory(position: Int) = viewModelScope.launch {
        try {
            clearHistorySelectedPosition()

            val taskHistory = history[position] as TaskHistoryUiModel

            history.removeAt(position)
            removeHistoryItem.postValue(position)

            val history = taskHistoryUiModelToTaskHistoryMapper.map(taskHistory)
            deleteHistoryUseCase(history)

            delay(1000)

            loadHistoryTasks()
        } catch (e: Exception) {
            logError(e)
            updateContentViews()
        }
    }

    private fun loadHistoryTasks() = viewModelScope.launch {
        try {
            taskMetrics.value = withContext(Dispatchers.IO) { getTaskMetricsUseCase(filter) }

            val historyView = withContext(Dispatchers.IO) { getHistoryViewUseCase(filter) }
            history.clear()
            history.addAll(historyView)

            updateContentViews()

        } catch (e: Exception) {
            logError(e)
            updateContentViews()
        }
    }

    private fun updateContentViews() {
        refreshHistory()
        updateSubtitle()
        updateTaskMetrics()
    }

    private fun updateTaskMetrics() {

    }

    private fun updateSubtitle() {
        if (dateRangeType === DateFilterTaskHistorySelectableItem.NO_FILTER) {
            subtitle.value = ""
            return
        }

        if (dateRangeType === DateFilterTaskHistorySelectableItem.DATE_RANGE) {
            setDateRangeSubtitle()
        } else {
            setDateFilterSubtitle()
        }
    }

    private fun setDateFilterSubtitle() {
        subtitle.value = getDateFilterNameViewUseCase(dateRangeType)
    }

    private fun setDateRangeSubtitle() = with(filter) {
        val initial = initialDate?.run { getShortDateViewUseCase(this) }.orEmpty()
        val final = finalDate?.run { getShortDateViewUseCase(this) }.orEmpty()
        subtitle.value = "$initial - $final"
    }

    private fun logError(e: Exception) = logError.apply { value = e }
    private fun refreshHistory() = refreshHistory.apply { value = Unit }

    private fun updateHistoryStatus(position: Int, status: TaskStatus) = viewModelScope.launch {
        try {
            clearHistorySelectedPosition()

            val historyView = history[position] as TaskHistoryUiModel
            historyView.done = status == TaskStatus.DONE

            updateHistoryItem(position)
            val history = taskHistoryUiModelToTaskHistoryMapper.map(historyView)
            updateHistoryUseCase(history)
            taskMetrics.value = getTaskMetricsUseCase(filter)

        } catch (e: Exception) {
            logError(e)
            updateContentViews()
        }
    }

    private fun clearHistorySelectedPosition() {
        selectedItemPosition = -1
    }

    private fun updateHistoryItem(position: Int) {
        updateHistoryItem.value = position
    }

    private fun scrollTo(position: Int) {
        scrollTo.value = position
    }

    private fun hasHistorySelected(): Boolean {
        return selectedItemPosition != -1
    }

    override fun dispatchViewAction(viewAction: TaskHistoryViewAction) {
        TODO("Not yet implemented")
    }

}