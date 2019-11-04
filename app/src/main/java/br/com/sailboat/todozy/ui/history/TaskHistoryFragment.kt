package br.com.sailboat.todozy.ui.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPFragment
import br.com.sailboat.todozy.ui.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.ui.dialog.selectable.*
import org.koin.android.ext.android.inject
import java.util.*

class TaskHistoryFragment: BaseMVPFragment<TaskHistoryContract.Presenter>(), TaskHistoryContract.View {

    override val presenter: TaskHistoryContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_history

    private var tvDoneTasks: TextView? = null
    private var tvNotDoneTasks: TextView? = null

    fun newInstance(taskId: Long?): TaskHistoryFragment {
        val bundle = Bundle()

        if (taskId != null) {
//            ExtrasHelper.putTaskId(taskId, bundle)
        }

        val fragment = TaskHistoryFragment()
        fragment.arguments = bundle

        return fragment
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_history_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
//                presenter.onClickMenuFilter()
                return true
            }
            R.id.menu_clear_history -> {
//                presenter.onClickMenuClearHistory()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun showFilterDialog(filter: TaskHistoryFilter) {
//        TaskHistoryFilterDialog.show(fragmentManager, filter, object : TaskHistoryFilterDialog.Callback {
//            override fun onClickFilterDate() {
//                presenter.onClickFilterDate()
//            }
//
//            override fun onClickFilterStatus() {
//                presenter.onClickFilterStatus()
//            }
//        })
    }

    override fun setEmptySubtitle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDateRangeSubtitle(initialDate: Calendar?, finalDate: Calendar?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshHistoryItem(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun scrollTo(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeHistoryItem(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun scrollToTop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTaskId(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initViews() {
        initMetricView()
    }

    override fun showDialogClearHistory() {
        SelectItemDialog.show(fragmentManager!!, getString(R.string.clear_history), ClearTaskHistorySelectableItem.getItems(), null, object: SelectItemDialog.Callback {
            override fun onClickItem(item: SelectableItem) {
                onClickClearHistory(item)
            }
        })

    }

    override fun setDoneTasks(doneTasks: String) {
        tvDoneTasks!!.text = doneTasks
    }

    override fun setNotDoneTasks(notDoneTasks: String) {
        tvNotDoneTasks!!.text = notDoneTasks
    }

    override fun showDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
//        DateRangeSelectorDialog.show(fragmentManager, initialDate, finalDate) { initialDate, finalDate -> presenter.onClickOkDateRangeSelectorDialog(initialDate, finalDate) }

    }

    override fun showStatusFilterDialog(status: TaskStatusSelectableItem) {
//        SelectItemDialog.show(fragmentManager, getString(R.string.filter_status), TaskStatusSelectableItem.getItems(), status, SelectItemDialog.Callback { item -> presenter.onClickFilterStatusItem(item as TaskStatusSelectableItem) })
    }

    override fun startTaskHistoryDetails(taskHistoryId: Long) {
//        TaskHistoryDetailsActivity.start(this, taskHistoryId)
    }

    override fun showDateFilterDialog(selectedFilter: DateFilterTaskHistorySelectableItem) {
        SelectItemDialog.show(fragmentManager!!, getString(R.string.filter), DateFilterTaskHistorySelectableItem.getItems(), selectedFilter, object: SelectItemDialog.Callback {
            override fun onClickItem(item: SelectableItem) {
                onClickFilterDate(item)
            }
        })
    }

    override fun showClearHistoryKeepAmountDialog() {
        val dialog = TwoOptionsDialog()
        dialog.message = getString(R.string.msg_ask_clear_history_keep)
        dialog.positiveMsg = R.string.clear
//        dialog.positiveCallback = TwoOptionsDialog.PositiveCallback { presenter.onClickYesClearHistoryKeepAmountDialog() }
        dialog.show(fragmentManager!!, "CLEAR_HISTORY")
    }

    override fun showClearAllHistoryDialog() {
        val dialog = TwoOptionsDialog()
        dialog.message = getString(R.string.msg_ask_clear_all_history)
        dialog.positiveMsg = R.string.clear
//        dialog.positiveCallback = TwoOptionsDialog.PositiveCallback { presenter.onClickYesClearAllHistoryDialog() }
        dialog.show(fragmentManager!!, "CLEAR_HISTORY")
    }

    override fun showDeleteItemDialog(position: Int) {
//        DialogHelper.showDeleteDialog(fragmentManager, activity!!) { presenter.onClickYesDeleteHistory(position) }

    }

    override fun startInsertTaskActivity(id: Long) {
//        InsertTaskActivity.startToEdit(this, id)
    }

//    protected override fun onInitToolbar() {
//        toolbar.setTitle(R.string.history)
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
//        toolbar.setNavigationOnClickListener(View.OnClickListener { activity!!.onBackPressed() })
//    }

//    protected override fun onInitRecycler() {
//        recycler.setAdapter(TaskHistoryListAdapter(presenter))
//    }

    private fun initMetricView() {
        tvDoneTasks = view!!.findViewById<View>(R.id.tvMetricsDone) as TextView
        tvNotDoneTasks = view!!.findViewById<View>(R.id.tvMetricsNotDone) as TextView
    }

//    private fun getLinLayManager(): LinearLayoutManager {
//        return recycler.getLayoutManager()
//    }

    private fun onClickFilterDate(item: SelectableItem) {
        val selectedItem = item as DateFilterTaskHistorySelectableItem

        when (selectedItem) {
            DateFilterTaskHistorySelectableItem.NO_FILTER -> {
//                presenter.onClickFilterNoFilter()
                return
            }
            DateFilterTaskHistorySelectableItem.TODAY -> {
//                presenter.onClickFilterToday()
                return
            }
            DateFilterTaskHistorySelectableItem.YESTERDAY -> {
//                presenter.onClickFilterYesterday()
                return
            }
            DateFilterTaskHistorySelectableItem.LAST_7_DAYS -> {
//                presenter.onClickFilterLastSevenDays()
                return
            }
            DateFilterTaskHistorySelectableItem.LAST_30_DAYS -> {
//                presenter.onClickFilterLastThirtyDays()
                return
            }
            DateFilterTaskHistorySelectableItem.DATE_RANGE -> {
//                presenter.onClickFilterDateRange()
                return
            }
        }
    }

    private fun onClickClearHistory(item: SelectableItem) {
        when (item as ClearTaskHistorySelectableItem) {
            ClearTaskHistorySelectableItem.CLEAR_HISTORY_KEEP_AMOUNT -> {
//                presenter.onClickClearHistoryKeepAmount()
                return
            }
            ClearTaskHistorySelectableItem.CLEAR_ALL_HISTORY -> {
//                presenter.onClickAllHistory()
                return
            }
        }
    }

    private fun onClickFilterStatus(item: SelectableItem) {
        when (item as TaskStatusSelectableItem) {
            TaskStatusSelectableItem.NO_FILTER -> {
//                presenter.onClickClearHistoryKeepAmount()
                return
            }
            TaskStatusSelectableItem.TASKS_DONE -> {
//                presenter.onClickAllHistory()
                return
            }
            TaskStatusSelectableItem.TASKS_NOT_DONE -> {
//                presenter.onClickAllHistory()
                return
            }
        }
    }
}