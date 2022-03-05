package br.com.sailboat.todozy.feature.task.history.presentation

import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.DeleteHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.UpdateHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.todozy.uicomponent.model.TaskHistoryUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.mvp.BasePresenter
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.delay
import java.util.*

class TaskHistoryPresenter(
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getHistoryViewUseCase: GetHistoryViewUseCase,
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase,
    private val taskHistoryUiModelToTaskHistoryMapper: TaskHistoryUiModelToTaskHistoryMapper,
) : BasePresenter<TaskHistoryContract.View>(), TaskHistoryContract.Presenter {

    private var taskId = Entity.NO_ID
    private val history = mutableListOf<UiModel>()
    private var selectedItemPosition: Int = 0
    private var filter: TaskHistoryFilter = TaskHistoryFilter()
    private var taskMetrics: TaskMetrics? = null
    private var dateRangeType = DateFilterTaskHistorySelectableItem.NO_FILTER
    private var statusSelectableItem = TaskStatusSelectableItem.NO_FILTER

    override fun onStart() {
        extractTaskId()
        clearHistorySelectedPosition()
        loadHistoryTasks()
    }

    override fun onRestart() {
        updateContentViews()
    }

    override fun onClickHistory(position: Int) {
        when {
            isShowingOptions(position) -> {
                clearHistorySelectedPosition()
                view?.updateHistoryItem(position)
            }
            hasHistorySelected() -> {
                val oldSelectedPosition = selectedItemPosition
                selectedItemPosition = position
                view?.updateHistoryItem(oldSelectedPosition)
                view?.updateHistoryItem(position)
                view?.scrollTo(position)
            }
            else -> {
                selectedItemPosition = position
                view?.updateHistoryItem(position)
                view?.scrollTo(position)
            }
        }

    }

    override fun isShowingOptions(position: Int): Boolean {
        return selectedItemPosition == position
    }

    override fun onClickDelete(position: Int) {
        view?.showDeleteItemDialog(position)
    }

    override fun onSubmitSearch(search: String) {
        filter.text = search
        loadHistoryTasks()
    }

    override fun onClickFilterDate(dateRangeTypeSelected: DateFilterTaskHistorySelectableItem) {
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

    override fun onClickMarkTaskAsDone(position: Int) {
        updateHistoryStatus(position, TaskStatus.DONE)
    }

    override fun onClickMarkTaskAsNotDone(position: Int) {
        updateHistoryStatus(position, TaskStatus.NOT_DONE)
    }

    override fun onClickYesDeleteHistory(position: Int) = launchMain {
        try {
            clearHistorySelectedPosition()

            val taskHistory = history[position] as TaskHistoryUiModel

            history.removeAt(position)
            view?.removeHistoryItem(position)

            val history = taskHistoryUiModelToTaskHistoryMapper.map(taskHistory)
            deleteHistoryUseCase(history)

            delay(1000)

            loadHistoryTasks()
        } catch (e: Exception) {
            view?.log(e)
            updateContentViews()
        }
    }

    override fun onClickMenuFilter() {
        view?.showFilterDialog(dateRangeType, statusSelectableItem)
    }

    override fun onClickOkDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        filter.initialDate = initialDate
        filter.finalDate = finalDate
        dateRangeType = DateFilterTaskHistorySelectableItem.DATE_RANGE

        loadHistoryTasks()
        view?.scrollToTop()
    }

    override fun onClickYesClearAllHistory() = launchMain {
        try {
            clearHistorySelectedPosition()
            deleteAllHistoryUseCase()
            loadHistoryTasks()
        } catch (e: Exception) {
            view?.log(e)
            updateContentViews()
        }
    }

    override fun onClickFilterNoFilter() {
        filter.initialDate = null
        filter.finalDate = null
        loadHistoryTasks()
    }

    override fun onClickFilterToday() {
        val initial = Calendar.getInstance()
        initial.clearTime()

        val finalTime = Calendar.getInstance()
        finalTime.setFinalTimeToCalendar()

        filter.initialDate = initial
        filter.finalDate = finalTime

        loadHistoryTasks()

    }

    override fun onClickFilterYesterday() {
        val initial = Calendar.getInstance()
        initial.add(Calendar.DAY_OF_MONTH, -1)
        initial.clearTime()

        val finalTime = Calendar.getInstance()
        finalTime.add(Calendar.DAY_OF_MONTH, -1)
        finalTime.setFinalTimeToCalendar()

        filter.initialDate = initial
        filter.finalDate = finalTime

        loadHistoryTasks()
    }

    override fun onClickFilterLastSevenDays() {
        val initial = Calendar.getInstance()
        initial.add(Calendar.DAY_OF_MONTH, -7)
        initial.clearTime()

        val finalTime = Calendar.getInstance()
        finalTime.setFinalTimeToCalendar()

        filter.initialDate = initial
        filter.finalDate = finalTime

        loadHistoryTasks()
    }

    override fun onClickFilterLastThirtyDays() {
        val initial = Calendar.getInstance()
        initial.add(Calendar.DAY_OF_MONTH, -30)
        initial.clearTime()

        val finalTime = Calendar.getInstance()
        finalTime.setFinalTimeToCalendar()

        filter.initialDate = initial
        filter.finalDate = finalTime

        loadHistoryTasks()
    }

    override fun onClickFilterDateRange() {
        val initial = filter.initialDate ?: Calendar.getInstance()
        val final = filter.finalDate ?: Calendar.getInstance()

        view?.showDateRangeSelectorDialog(initial, final)
    }

    override fun onClickCleanAllHistory() {
        view?.showConfirmationClearAllHistory()
    }

    private fun loadHistoryTasks() = launchMain {
        try {
            taskMetrics = getTaskMetricsUseCase(filter)

            val historyView = getHistoryViewUseCase(filter)
            history.clear()
            history.addAll(historyView)

            updateContentViews()

        } catch (e: Exception) {
            view?.log(e)
            updateContentViews()
        }
    }

    private fun updateContentViews() {
        updateSubtitle()
        view?.updateAllItems()
        updateHistoryVisibility()
        updateMetricsView()
    }

    private fun updateMetricsView() {
        val metrics = taskMetrics

        if (metrics != null) {
            view?.setDoneTasks(metrics.doneTasks.toString())
            view?.setNotDoneTasks(metrics.notDoneTasks.toString())
        } else {
            view?.setDoneTasks("0")
            view?.setNotDoneTasks("0")
        }
    }

    private fun updateSubtitle() {
        val initialDate = filter.initialDate
        val finalDate = filter.finalDate

        if (dateRangeType === DateFilterTaskHistorySelectableItem.NO_FILTER) {
            view?.setEmptySubtitle()
            return
        }

        if (dateRangeType === DateFilterTaskHistorySelectableItem.DATE_RANGE) {
            view?.setDateRangeSubtitle(initialDate!!, finalDate!!)
        } else {
            view?.setDateFilterSubtitle(dateRangeType)
        }

    }

    private fun updateHistoryVisibility() {
        if (isTasksEmpty()) {
            view?.hideHistory()
            view?.showEmptyView()
        } else {
            view?.showHistory()
            view?.hideEmptyView()
        }
    }

    private fun isTasksEmpty(): Boolean {
        return history.isEmpty()
    }


    private fun updateHistoryStatus(position: Int, status: TaskStatus) = launchMain {
        try {
            clearHistorySelectedPosition()

            val historyView = history[position] as TaskHistoryUiModel
            historyView.done = status == TaskStatus.DONE

            view?.updateHistoryItem(position)

            val history = taskHistoryUiModelToTaskHistoryMapper.map(historyView)
            updateHistoryUseCase(history)

            taskMetrics = getTaskMetricsUseCase(filter)

            updateMetricsView()

        } catch (e: Exception) {
            view?.log(e)
            updateContentViews()
        }
    }

    private fun hasHistorySelected(): Boolean {
        return selectedItemPosition != -1
    }

    private fun clearHistorySelectedPosition() {
        selectedItemPosition = -1
    }

    override fun getHistoryViewList(): List<UiModel> {
        return history
    }

    override fun onClickFilterDate() {
        view?.showDateFilterDialog(dateRangeType)
    }

    override fun onClickFilterStatus() {
        view?.showStatusFilterDialog(statusSelectableItem)
    }

    override fun onClickFilterStatusItem(item: TaskStatusSelectableItem) {
        if (item === statusSelectableItem) {
            return
        }
        statusSelectableItem = item

        when (statusSelectableItem) {
            TaskStatusSelectableItem.NO_FILTER -> filter.status = null
            TaskStatusSelectableItem.TASKS_DONE -> filter.status = TaskStatus.DONE
            TaskStatusSelectableItem.TASKS_NOT_DONE -> filter.status = TaskStatus.NOT_DONE
        }

        loadHistoryTasks()
    }

    private fun extractTaskId() {
        taskId = view?.getTaskId() ?: Entity.NO_ID
    }

}