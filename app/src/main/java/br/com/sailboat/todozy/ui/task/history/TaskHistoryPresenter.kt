package br.com.sailboat.todozy.ui.task.history

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.clearTime
import br.com.sailboat.todozy.domain.helper.setFinalTimeToCalendar
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.tasks.GetTaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.TaskHistoryView
import kotlinx.coroutines.runBlocking
import java.util.*

class TaskHistoryPresenter(
        private val getTaskMetrics: GetTaskMetrics,
        private val getHistoryView: GetHistoryView) :
        BasePresenter<TaskHistoryContract.View>(), TaskHistoryContract.Presenter {

    private var taskId = EntityHelper.NO_ID
    private val history = mutableListOf<ItemView>()
    private var selectedItemPosition: Int = 0
    private var filter: TaskHistoryFilter = TaskHistoryFilter()
    private var taskMetrics: TaskMetrics? = null

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

    override fun checkIfTaskDesabled(position: Int): Boolean {
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
//        view?.scrollToTop()
    }

    override fun onClickMarkTaskAsDone(position: Int) {
        updateHistoryStatus(position, TaskStatus.DONE)
    }

    override fun onClickMarkTaskAsNot(position: Int) {
        updateHistoryStatus(position, TaskStatus.NOT_DONE)
    }

    fun onClickYesDeleteHistory(position: Int) {
        try {
//            val taskHistory = getHistory()[position] as TaskHistoryView
            clearHistorySelectedPosition()
            // TaskHistorySQLite.newInstance(getContext()).delete(taskHistory.getId())

//            getHistory().removeAt(position)
            view?.removeHistoryItem(position)

            loadHistoryTasks()
        } catch (e: Exception) {
            view?.log(e)
        }

    }


//    override fun submitTextForSearch(text: String) {
//        filter.text = text
//        loadHistoryTasks() // handle the previus launched async operations?
//        view?.scrollToTop()
//    }

    override fun onClickMenuClearHistory() {
        view?.showDialogClearHistory()
    }

    override fun onClickMenuFilter() {
        //        view?.showDateFilterDialog(getFilter().getDate());
        view?.showFilterDialog(filter)
    }

    override fun onClickOkDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        filter.initialDate = initialDate
        filter.finalDate = finalDate
        filter.date = DateFilterTaskHistorySelectableItem.DATE_RANGE

        loadHistoryTasks()
        view?.scrollToTop()
    }

    override fun onClickYesClearHistoryKeepAmountDialog() {
        setAllHistoryAsDisabled()
        loadHistoryTasks()
    }

    override fun onClickYesClearAllHistoryDialog() {
        deleteAllHistory()
        loadHistoryTasks()
    }

    override fun onClickFilterNoFilter() {
        if (filter.date !== DateFilterTaskHistorySelectableItem.NO_FILTER) {
            filter.date = DateFilterTaskHistorySelectableItem.NO_FILTER
            filter.initialDate = null
            filter.finalDate = null

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    override fun onClickFilterToday() {
        if (filter.date !== DateFilterTaskHistorySelectableItem.TODAY) {
            filter.date = DateFilterTaskHistorySelectableItem.TODAY

            val initial = Calendar.getInstance()
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.setFinalTimeToCalendar()

            filter.initialDate = initial
            filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    override fun onClickFilterYesterday() {
        if (filter.date !== DateFilterTaskHistorySelectableItem.YESTERDAY) {
            filter.date = DateFilterTaskHistorySelectableItem.YESTERDAY

            val initial = Calendar.getInstance()
            initial.add(Calendar.DAY_OF_MONTH, -1)
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.add(Calendar.DAY_OF_MONTH, -1)
            finalTime.setFinalTimeToCalendar()

            filter.initialDate = initial
            filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    override fun onClickFilterLastSevenDays() {
        if (filter.date !== DateFilterTaskHistorySelectableItem.LAST_7_DAYS) {
            filter.date = DateFilterTaskHistorySelectableItem.LAST_7_DAYS

            val initial = Calendar.getInstance()
            initial.add(Calendar.DAY_OF_MONTH, -7)
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.setFinalTimeToCalendar()

            filter.initialDate = initial
            filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    override fun onClickFilterLastThirtyDays() {
        if (filter.date !== DateFilterTaskHistorySelectableItem.LAST_30_DAYS) {
            filter.date = DateFilterTaskHistorySelectableItem.LAST_30_DAYS

            val initial = Calendar.getInstance()
            initial.add(Calendar.DAY_OF_MONTH, -30)
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.setFinalTimeToCalendar()

            filter.initialDate = initial
            filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    override fun onClickFilterDateRange() {
        // view?.showDateRangeSelectorDialog(filter.initialDate, filter.finalDate)
    }

    override fun onClickClearHistoryKeepAmount() {
        view?.showClearHistoryKeepAmountDialog()
    }

    override fun onClickAllHistory() {
        view?.showClearAllHistoryDialog()
    }

    override fun onClickYesDeleteHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadHistoryTasks() = launchAsync {
        try {
            // TODO: Filter metrics by select filter
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
        updateTasksVisibility()
        updateMetrics()
    }

    private fun updateMetrics() {
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

        if (filter.date === DateFilterTaskHistorySelectableItem.NO_FILTER) {
            view?.setEmptySubtitle()
            return
        }

        if (filter.date !== DateFilterTaskHistorySelectableItem.DATE_RANGE) {
//            view?.setSubtitle(getString(filter.date.getName()).toUpperCase())
        } else {
            view?.setDateRangeSubtitle(initialDate!!, finalDate!!)
        }

    }

    private fun updateTasksVisibility() {
        if (isTasksEmpty()) {
            view?.hideHistory()
//            view?.showEmptyView()
//            view?.expandToolbar()
        } else {
            view?.showHistory()
//            view?.hideEmptyView()
        }
    }

    private fun isTasksEmpty(): Boolean {
        return history.isEmpty()
    }


    private fun updateHistoryStatus(position: Int, status: TaskStatus) {
//        try {
//            clearHistorySelectedPosition()
//
//            val history = getHistory()[position] as TaskHistory
//            history.setStatus(status)
//
//            view?.updateRecyclerItemChanged(position)
//
//            TaskHistorySQLite.newInstance(getContext()).update(history)
//
//            loadHistoryTasks()
//
//        } catch (e: Exception) {
//            printLogAndShowDialog(e)
//        }

    }

    private fun hasHistorySelected(): Boolean {
        return selectedItemPosition != -1
    }

    private fun clearHistorySelectedPosition() {
        selectedItemPosition = -1
    }

    private fun setAllHistoryAsDisabled() {
//        try {
//            TaskHistorySQLite.newInstance(getContext()).setAllHistoryDisabled()
//        } catch (e: Exception) {
//            printLogAndShowDialog(e)
//        }

    }

    private fun deleteAllHistory() {
//        try {
//            TaskHistorySQLite.newInstance(getContext()).deleteAllHistory()
//        } catch (e: Exception) {
//            printLogAndShowDialog(e)
//        }

    }

    override fun getHistoryViewList(): List<ItemView> {
        return history
    }

    override fun onClickFilterDate() {
        view?.showDateFilterDialog(filter.date)
    }

    override fun onClickFilterStatus() {
        view?.showStatusFilterDialog(filter.status)
    }

    override fun onClickFilterStatusItem(item: TaskStatusSelectableItem) {
        filter.status = item
        loadHistoryTasks()
    }

    private fun extractTaskId() {
        taskId = view?.getTaskId() ?: EntityHelper.NO_ID
    }

}