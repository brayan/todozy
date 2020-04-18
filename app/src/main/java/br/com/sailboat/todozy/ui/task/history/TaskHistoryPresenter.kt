package br.com.sailboat.todozy.ui.task.history

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.clearTime
import br.com.sailboat.todozy.domain.helper.setFinalTimeToCalendar
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.tasks.GetTaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.ui.model.TaskHistoryView
import java.util.*

class TaskHistoryPresenter(
        private val getTaskMetrics: GetTaskMetrics,
        private val getHistoryView: GetHistoryView) :
        BasePresenter<TaskHistoryContract.View>(), TaskHistoryContract.Presenter {

    private val viewModel by lazy { TaskHistoryViewModel() }
    override val history by lazy { viewModel.history }

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
                val oldSelectedPosition = viewModel.selectedItemPosition
                viewModel.selectedItemPosition = position
                view?.updateHistoryItem(oldSelectedPosition)
                view?.updateHistoryItem(position)
                view?.scrollTo(position)
            }
            else -> {
                viewModel.selectedItemPosition = position
                view?.updateHistoryItem(position)
                view?.scrollTo(position)
            }
        }

    }

    override fun isShowingOptions(position: Int): Boolean {
        return viewModel.selectedItemPosition == position
    }

    override fun onClickDelete(position: Int) {
        view?.showDeleteItemDialog(position)
    }

    override fun checkIfTaskDesabled(position: Int): Boolean {
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
//        viewModel.filter.text = text
//        loadHistoryTasks() // handle the previus launched async operations?
//        view?.scrollToTop()
//    }

    override fun onClickMenuClearHistory() {
        view?.showDialogClearHistory()
    }

    override fun onClickMenuFilter() {
        //        view?.showDateFilterDialog(viewModel.getFilter().getDate());
        view?.showFilterDialog(viewModel.filter)
    }

    override fun onClickOkDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        viewModel.filter.initialDate = initialDate
        viewModel.filter.finalDate = finalDate
        viewModel.filter.date = DateFilterTaskHistorySelectableItem.DATE_RANGE

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
        if (viewModel.filter.date !== DateFilterTaskHistorySelectableItem.NO_FILTER) {
            viewModel.filter.date = DateFilterTaskHistorySelectableItem.NO_FILTER
            viewModel.filter.initialDate = null
            viewModel.filter.finalDate = null

            loadHistoryTasks()
            view?.scrollToTop()
        }
    }

    override fun onClickFilterToday() {
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

    override fun onClickFilterYesterday() {
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

    override fun onClickFilterLastSevenDays() {
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

    override fun onClickFilterLastThirtyDays() {
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

    override fun onClickFilterDateRange() {
        // view?.showDateRangeSelectorDialog(viewModel.filter.initialDate, viewModel.filter.finalDate)
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
            val taskMetrics = getTaskMetrics(viewModel.taskId)
            viewModel.taskMetrics = taskMetrics

            val history = getHistoryView(viewModel.filter)
            viewModel.history.clear()
            viewModel.history.addAll(history)

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
        val metrics = viewModel.taskMetrics

        if (metrics != null) {
            view?.setDoneTasks(metrics.doneTasks.toString())
            view?.setNotDoneTasks(metrics.notDoneTasks.toString())
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
            view?.setDateRangeSubtitle(initialDate!!, finalDate!!)
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

    override fun onClickFilterDate() {
        view?.showDateFilterDialog(viewModel.filter.date)
    }

    override fun onClickFilterStatus() {
        view?.showStatusFilterDialog(viewModel.filter.status)
    }

    override fun onClickFilterStatusItem(item: TaskStatusSelectableItem) {
        viewModel.filter.status = item
        loadHistoryTasks()
    }

    private fun extractTaskId() {
        viewModel.taskId = view?.getTaskId() ?: EntityHelper.NO_ID
    }

}