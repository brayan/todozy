package br.com.sailboat.todozy.ui.history

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.clearTime
import br.com.sailboat.todozy.domain.helper.setFinalTimeToCalendar
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.tasks.GetTask
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.ui.model.TaskHistoryView
import java.util.*

class TaskHistoryPresenter(private val getTask: GetTask) : BasePresenter<TaskHistoryContract.View>(), TaskHistoryContract.Presenter {

    private val viewModel by lazy { TaskHistoryViewModel() }

    override fun onStart() {
        extractTaskId()
        clearHistorySelectedPosition()
        loadHistoryTasks()
    }

    override fun onRestart() {
        updateContentViews()
    }

    fun onClickHistory(position: Int) {
        if (isShowingOptions(position)) {
            clearHistorySelectedPosition()
            view?.refreshHistoryItem(position)

        } else if (hasHistorySelected()) {
            val oldSelectedPosition = viewModel.selectedItemPosition
            viewModel.selectedItemPosition = position
            view?.refreshHistoryItem(oldSelectedPosition)
            view?.refreshHistoryItem(position)
            view?.scrollTo(position)

        } else {
            viewModel.selectedItemPosition = position
            view?.refreshHistoryItem(position)
            view?.scrollTo(position)
        }

    }

    fun isShowingOptions(position: Int): Boolean {
        return viewModel.selectedItemPosition == position
    }

    fun onClickDelete(position: Int) {
        view?.showDeleteItemDialog(position)
    }

    fun checkIfTaskDesabled(position: Int): Boolean {
        try {
            val history = viewModel.history[position] as? TaskHistoryView
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


    fun onClickMarkTaskAsDone(position: Int) {
        updateHistoryStatus(position, TaskStatus.DONE)
    }

    fun onClickMarkTaskAsNot(position: Int) {
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
//        viewModel.filter.text = text
//        loadHistoryTasks() // handle the previus launched async operations?
//        view?.scrollToTop()
//    }

    fun onClickMenuClearHistory() {
        view?.showDialogClearHistory()
    }

    fun onClickMenuFilter() {
        //        view?.showDateFilterDialog(viewModel.getFilter().getDate());
        view?.showFilterDialog(viewModel.filter)
    }

    fun onClickOkDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        viewModel.filter.initialDate = initialDate
        viewModel.filter.finalDate = finalDate
        viewModel.filter.date = DateFilterTaskHistorySelectableItem.DATE_RANGE

        loadHistoryTasks()
        view?.scrollToTop()
    }

    fun onClickYesClearHistoryKeepAmountDialog() {
        setAllHistoryAsDisabled()
        loadHistoryTasks()
    }

    fun onClickYesClearAllHistoryDialog() {
        deleteAllHistory()
        loadHistoryTasks()
    }

    fun onClickFilterNoFilter() {
        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.NO_FILTER) {
            viewModel.filter.date = DateFilterTaskHistorySelectableItem.NO_FILTER
            viewModel.filter.initialDate = null
            viewModel.filter.finalDate = null

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    fun onClickFilterToday() {
        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.TODAY) {
            viewModel.filter.date = DateFilterTaskHistorySelectableItem.TODAY

            val initial = Calendar.getInstance()
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.setFinalTimeToCalendar()

            viewModel.filter.initialDate = initial
            viewModel.filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    fun onClickFilterYesterday() {
        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.YESTERDAY) {
            viewModel.filter.date = DateFilterTaskHistorySelectableItem.YESTERDAY

            val initial = Calendar.getInstance()
            initial.add(Calendar.DAY_OF_MONTH, -1)
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.add(Calendar.DAY_OF_MONTH, -1)
            finalTime.setFinalTimeToCalendar()

            viewModel.filter.initialDate = initial
            viewModel.filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    fun onClickFilterLastSevenDays() {
        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.LAST_7_DAYS) {
            viewModel.filter.date = DateFilterTaskHistorySelectableItem.LAST_7_DAYS

            val initial = Calendar.getInstance()
            initial.add(Calendar.DAY_OF_MONTH, -7)
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.setFinalTimeToCalendar()

            viewModel.filter.initialDate = initial
            viewModel.filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    fun onClickFilterLastThirtyDays() {
        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.LAST_30_DAYS) {
            viewModel.filter.date = DateFilterTaskHistorySelectableItem.LAST_30_DAYS

            val initial = Calendar.getInstance()
            initial.add(Calendar.DAY_OF_MONTH, -30)
            initial.clearTime()

            val finalTime = Calendar.getInstance()
            finalTime.setFinalTimeToCalendar()

            viewModel.filter.initialDate = initial
            viewModel.filter.finalDate = finalTime

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    fun onClickFilterDateRange() {
        // view?.showDateRangeSelectorDialog(viewModel.filter.initialDate, viewModel.filter.finalDate)
    }

    fun onClickClearHistoryKeepAmount() {
        view?.showClearHistoryKeepAmountDialog()
    }

    fun onClickAllHistory() {
        view?.showClearAllHistoryDialog()
    }

    private fun loadHistoryTasks() = launchAsync {

        try {
//            history = TaskHistoryLoader.loadHistory(getContext(), viewModel.filter)
//            taskMetrics = TaskMetricsLoader.getMetrics(getContext(), viewModel.filter)
//
//            getHistory().clear()
//            getHistory().addAll(history)
//            viewModel.taskMetrics = taskMetrics
//            updateContentViews()

        } catch(e: Exception) {
//            printLogAndShowDialog(e)
//            updateContentViews()
        }
    }

    private fun updateContentViews() {
        updateSubtitle()
//        view?.updateRecycler()
        updateTasksVisibility()
        updateAmountOfTasks()
    }

    private fun updateAmountOfTasks() {
        val metrics = viewModel.taskMetrics

        if (metrics != null) {
//            view?.setDoneTasks(String.valueOf(metrics.getDoneTasks()))
//            view?.setNotDoneTasks(String.valueOf(metrics.getNotDoneTasks()))
        } else {
            view?.setDoneTasks("0")
            view?.setNotDoneTasks("0")
        }

    }

    private fun updateSubtitle() {
        val initialDate = viewModel.filter.initialDate
        val finalDate = viewModel.filter.finalDate

        if (viewModel.filter.date === DateFilterTaskHistorySelectableItem.NO_FILTER) {
            view?.setEmptySubtitle()
            return
        }

        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.DATE_RANGE) {
//            view?.setSubtitle(getString(viewModel.filter.date.getName()).toUpperCase())
        } else {
            // view?.setSubtitle(DateHelper.getShortDate(getContext(), initialDate) + " - " + DateHelper.getShortDate(getContext(), finalDate))
            view?.setDateRangeSubtitle(initialDate, finalDate)
        }

    }

    private fun updateTasksVisibility() {
//        if (isTasksEmpty()) {
//            view?.hideRecycler()
//            view?.showEmptyView()
//            view?.expandToolbar()
//        } else {
//            view?.showRecycler()
//            view?.hideEmptyView()
//        }
    }

    private fun isTasksEmpty(): Boolean {
        return viewModel.history.isEmpty()
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
        return viewModel.selectedItemPosition != -1
    }

    private fun clearHistorySelectedPosition() {
        viewModel.selectedItemPosition = -1
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

    fun onClickFilterDate() {
        view?.showDateFilterDialog(viewModel.filter?.date)
    }

    fun onClickFilterStatus() {
        view?.showStatusFilterDialog(viewModel.filter?.status)
    }

    fun onClickFilterStatusItem(item: TaskStatusSelectableItem) {
        viewModel.filter?.status = item
        loadHistoryTasks()
    }

    private fun extractTaskId() {
        viewModel.taskId = view?.getTaskId() ?: EntityHelper.NO_ID
    }

}