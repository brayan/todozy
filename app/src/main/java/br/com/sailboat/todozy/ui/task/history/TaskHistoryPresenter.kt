package br.com.sailboat.todozy.ui.task.history

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.clearTime
import br.com.sailboat.todozy.domain.helper.setFinalTimeToCalendar
import br.com.sailboat.todozy.domain.history.DeleteAllHistory
import br.com.sailboat.todozy.domain.history.DeleteHistory
import br.com.sailboat.todozy.domain.history.UpdateHistory
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.tasks.GetTaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.ui.mapper.mapToTaskHistory
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.TaskHistoryView
import br.com.sailboat.todozy.ui.model.TaskStatusView
import kotlinx.coroutines.delay
import java.util.*

class TaskHistoryPresenter(
        private val getTaskMetrics: GetTaskMetrics,
        private val getHistoryView: GetHistoryView,
        private val updateHistory: UpdateHistory,
        private val deleteHistory: DeleteHistory,
        private val deleteAllHistory: DeleteAllHistory) :
        BasePresenter<TaskHistoryContract.View>(), TaskHistoryContract.Presenter {

    private var taskId = EntityHelper.NO_ID
    private val history = mutableListOf<ItemView>()
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

    override fun checkIfTaskDisabled(position: Int): Boolean {
        try {
            val history = history[position] as? TaskHistoryView
            // val task = getTask(history.tas)

            // TODO: IMPLEMENT ENABLE/DISABLE LOGIC
            //            if (!task.isEnabled()) {
            //                view?.startInsertTaskActivity(task.getId());
            //                return true;
            //            }

            return false

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    override fun onSubmitSearch(search: String) {
        filter.text = search
        loadHistoryTasks()
    }

    override fun onClickDateRange(dateRangeTypeSelected: DateFilterTaskHistorySelectableItem) {
        if (dateRangeTypeSelected === dateRangeType) {
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
        updateHistoryStatus(position, TaskStatusView.DONE)
    }

    override fun onClickMarkTaskAsNotDone(position: Int) {
        updateHistoryStatus(position, TaskStatusView.NOT_DONE)
    }

    override fun onClickYesDeleteHistory(position: Int) = launchAsync {
        try {
            clearHistorySelectedPosition()

            val taskHistory = history[position] as TaskHistoryView

            history.removeAt(position)
            view?.removeHistoryItem(position)

            deleteHistory(taskHistory.mapToTaskHistory())

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

    override fun onClickYesClearAllHistory() = launchAsync {
        try {
            clearHistorySelectedPosition()
            deleteAllHistory()
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
        // view?.showDateRangeSelectorDialog(filter.initialDate, filter.finalDate)
    }

    override fun onClickCleanAllHistory() {
        view?.showConfirmationClearAllHistory()
    }

    private fun loadHistoryTasks() = launchAsync {
        try {
            taskMetrics = getTaskMetrics(filter)

            val historyView = getHistoryView(filter)
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

        if (dateRangeType !== DateFilterTaskHistorySelectableItem.DATE_RANGE) {
//            view?.setSubtitle(getString(filter.date.getName()).toUpperCase())
        } else {
            view?.setDateRangeSubtitle(initialDate!!, finalDate!!)
        }

    }

    private fun updateHistoryVisibility() {
        if (isTasksEmpty()) {
            view?.hideHistory()
            view?.showEmptyView()
//            view?.expandToolbar()
        } else {
            view?.showHistory()
            view?.hideEmptyView()
        }
    }

    private fun isTasksEmpty(): Boolean {
        return history.isEmpty()
    }


    private fun updateHistoryStatus(position: Int, status: TaskStatusView) = launchAsync {
        try {
            clearHistorySelectedPosition()

            val historyView = history[position] as TaskHistoryView
            historyView.status = status

            view?.updateHistoryItem(position)

            updateHistory(historyView.mapToTaskHistory())

            taskMetrics = getTaskMetrics(filter)

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

    override fun getHistoryViewList(): List<ItemView> {
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
        taskId = view?.getTaskId() ?: EntityHelper.NO_ID
    }

}