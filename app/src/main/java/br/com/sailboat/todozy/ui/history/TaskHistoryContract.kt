package br.com.sailboat.todozy.ui.history

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPContract
import br.com.sailboat.todozy.ui.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.TaskStatusSelectableItem
import java.util.*

interface TaskHistoryContract {

    interface View : BaseMVPContract.View {
        fun showDialogClearHistory()
        fun setDoneTasks(doneTasks: String)
        fun setNotDoneTasks(notDoneTasks: String)
        fun showDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar)
        fun showDateFilterDialog(selectedFilter: DateFilterTaskHistorySelectableItem)
        fun showClearHistoryKeepAmountDialog()
        fun showClearAllHistoryDialog()
        fun showDeleteItemDialog(position: Int)
        fun startInsertTaskActivity(id: Long)
        fun showFilterDialog(filter: TaskHistoryFilter)
        fun showStatusFilterDialog(status: TaskStatusSelectableItem)
        fun startTaskHistoryDetails(taskHistoryId: Long)
        fun setEmptySubtitle()
        fun setDateRangeSubtitle(initialDate: Calendar?, finalDate: Calendar?)
        fun refreshHistoryItem(position: Int)
        fun scrollTo(position: Int)
        fun removeHistoryItem(position: Int)
        fun scrollToTop()
        fun getTaskId(): Long
    }

    interface Presenter : BaseMVPContract.Presenter {

    }


}