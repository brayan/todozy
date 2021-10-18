package br.com.sailboat.todozy.features.tasks.presentation.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.extensions.clearTime
import br.com.sailboat.todozy.core.extensions.setFinalTimeToCalendar
import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.core.presentation.helper.Event
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.TaskHistoryView
import br.com.sailboat.todozy.core.presentation.model.TaskStatusView
import br.com.sailboat.todozy.core.presentation.model.mapToTaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.DeleteHistoryUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.UpdateHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TaskHistoryViewModel(
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getHistoryViewUseCase: GetHistoryViewUseCase,
    private val getShortDateViewUseCase: GetShortDateViewUseCase,
    private val getDateFilterNameView: GetDateFilterNameView,
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase
) : BaseViewModel() {

    private var taskId = Entity.NO_ID
    val history = mutableListOf<ItemView>()
    private var selectedItemPosition: Int = 0
    private var filter: TaskHistoryFilter = TaskHistoryFilter()
    private var dateRangeType = DateFilterTaskHistorySelectableItem.NO_FILTER
    private var statusSelectableItem = TaskStatusSelectableItem.NO_FILTER

    val logError = MutableLiveData<Event<Exception>>()
    val refreshHistory = MutableLiveData<Event<Unit>>()
    val removeHistoryItem = MutableLiveData<Event<Int>>()
    val showFilter = MutableLiveData<Event<Unit>>()
    val showConfirmationClearAllHistory = MutableLiveData<Event<Unit>>()
    val showConfirmationDelete = MutableLiveData<Event<Int>>()
    val subtitle = MutableLiveData<String>()
    val doneTasks = MutableLiveData<Int>()
    val notDoneTasks = MutableLiveData<Int>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val updateHistoryItem = MutableLiveData<Event<Int>>()
    val scrollTo = MutableLiveData<Event<Int>>()

    fun start() {
        loadHistoryTasks()
    }

    fun onClickMenuFilter() {
        showFilter.value = Event(Unit)
    }

    fun onClickCleanAllHistory() {
        showConfirmationClearAllHistory.value = Event(Unit)
    }

    fun onClickMarkTaskAsDone(position: Int) {
        updateHistoryStatus(position, TaskStatusView.DONE)
    }

    fun onClickMarkTaskAsNotDone(position: Int) {
        updateHistoryStatus(position, TaskStatusView.NOT_DONE)
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
        showConfirmationDelete.value = Event(position)
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

            val taskHistory = history[position] as TaskHistoryView

            history.removeAt(position)
            removeHistoryItem.postValue(Event(position))

            deleteHistoryUseCase(taskHistory.mapToTaskHistory())

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
        subtitle.value = getDateFilterNameView(dateRangeType)
    }

    private fun setDateRangeSubtitle() = with(filter) {
        val initial = initialDate?.run { getShortDateViewUseCase(this) }.orEmpty()
        val final = finalDate?.run { getShortDateViewUseCase(this) }.orEmpty()
        subtitle.value = "$initial - $final"
    }

    private fun logError(e: Exception) = logError.apply { value = Event(e) }
    private fun refreshHistory() = refreshHistory.apply { value = Event(Unit) }

    private fun updateHistoryStatus(position: Int, status: TaskStatusView) = viewModelScope.launch {
        try {
            clearHistorySelectedPosition()

            val historyView = history[position] as TaskHistoryView
            historyView.status = status

            updateHistoryItem(position)
            updateHistoryUseCase(historyView.mapToTaskHistory())
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
        updateHistoryItem.value = Event(position)
    }

    private fun scrollTo(position: Int) {
        scrollTo.value = Event(position)
    }

    private fun hasHistorySelected(): Boolean {
        return selectedItemPosition != -1
    }

}