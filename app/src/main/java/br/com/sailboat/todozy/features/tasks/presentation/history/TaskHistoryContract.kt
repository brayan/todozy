package br.com.sailboat.todozy.features.tasks.presentation.history

import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPContract
import java.util.*

interface TaskHistoryContract {

    interface View : BaseMVPContract.View {
        fun setDoneTasks(doneTasks: String)
        fun setNotDoneTasks(notDoneTasks: String)
        fun showDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar)
        fun showDateFilterDialog(selectedFilter: DateFilterTaskHistorySelectableItem)
        fun showConfirmationClearAllHistory()
        fun showDeleteItemDialog(position: Int)
        fun startInsertTaskActivity(id: Long)
        fun showFilterDialog(
            dateRangeType: DateFilterTaskHistorySelectableItem,
            status: TaskStatusSelectableItem
        )

        fun showStatusFilterDialog(status: TaskStatusSelectableItem)
        fun setEmptySubtitle()
        fun setDateRangeSubtitle(initialDate: Calendar, finalDate: Calendar)
        fun updateHistoryItem(position: Int)
        fun updateAllItems()
        fun scrollTo(position: Int)
        fun removeHistoryItem(position: Int)
        fun scrollToTop()
        fun getTaskId(): Long
        fun hideHistory()
        fun showHistory()
        fun showEmptyView()
        fun hideEmptyView()
        fun setDateFilterSubtitle(dateRangeType: DateFilterTaskHistorySelectableItem)
    }

    interface Presenter : BaseMVPContract.Presenter {
        fun getHistoryViewList(): List<UiModel>
        fun onClickFilterDate()
        fun onClickFilterStatus()
        fun onClickMenuFilter()
        fun onClickCleanAllHistory()
        fun onClickFilterNoFilter()
        fun onClickFilterToday()
        fun onClickFilterYesterday()
        fun onClickFilterLastSevenDays()
        fun onClickFilterLastThirtyDays()
        fun onClickFilterDateRange()
        fun onClickOkDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar)

        // TODO: REPLACE SelectableItem
        fun onClickFilterStatusItem(item: TaskStatusSelectableItem)
        fun onClickYesClearAllHistory()
        fun onClickYesDeleteHistory(position: Int)
        fun onClickMarkTaskAsDone(position: Int)
        fun onClickMarkTaskAsNotDone(position: Int)
        fun onClickHistory(position: Int)
        fun isShowingOptions(position: Int): Boolean
        fun onClickDelete(position: Int)
        fun onSubmitSearch(search: String)
        fun onClickFilterDate(dateRangeTypeSelected: DateFilterTaskHistorySelectableItem)
    }


}