package br.com.sailboat.todozy.ui.task.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPFragment
import br.com.sailboat.todozy.ui.dialog.DateRangeSelectorDialog
import br.com.sailboat.todozy.ui.dialog.TaskHistoryFilterDialog
import br.com.sailboat.todozy.ui.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.ui.dialog.selectable.*
import br.com.sailboat.todozy.ui.helper.*
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.task.insert.InsertTaskActivity
import kotlinx.android.synthetic.main.appbar_task_history.*
import kotlinx.android.synthetic.main.ept_view.*
import kotlinx.android.synthetic.main.frg_task_history.*
import kotlinx.android.synthetic.main.task_metrics.*
import kotlinx.android.synthetic.main.toolbar_scroll.*
import org.koin.android.ext.android.inject
import java.util.*

class TaskHistoryFragment : BaseMVPFragment<TaskHistoryContract.Presenter>(), TaskHistoryContract.View {

    override val presenter: TaskHistoryContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_history

    private val linearLayoutManager by lazy { recycler.layoutManager as LinearLayoutManager }

    companion object {
        fun newInstance() = TaskHistoryFragment()

        fun newInstance(taskId: Long): TaskHistoryFragment = with(TaskHistoryFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_history_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> presenter.onClickMenuFilter()
            R.id.menu_clear_history -> presenter.onClickCleanAllHistory()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun initViews() {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.setTitle(R.string.history)

        recycler.run {
            adapter = TaskHistoryAdapter(object : TaskHistoryAdapter.Callback {
                override fun onClickMarkTaskAsDone(position: Int) = presenter.onClickMarkTaskAsDone(position)
                override fun onClickMarkTaskAsNotDone(position: Int) = presenter.onClickMarkTaskAsNotDone(position)
                override fun onClickHistory(position: Int) = presenter.onClickHistory(position)
                override fun isShowingOptions(position: Int) = presenter.isShowingOptions(position)
                override fun onClickDelete(position: Int) = presenter.onClickDelete(position)
                override val history: List<ItemView> = presenter.getHistoryViewList()
            })
            layoutManager = LinearLayoutManager(activity)
        }

        tvEmptyViewMessagePrimary.setText(R.string.no_history_found)
    }

    override fun onSubmitSearch(search: String) {
        presenter.onSubmitSearch(search)
    }

    override fun showFilterDialog(dateRangeType: DateFilterTaskHistorySelectableItem, status: TaskStatusSelectableItem) {
        TaskHistoryFilterDialog.show(fragmentManager!!, dateRangeType, status, object : TaskHistoryFilterDialog.Callback {
            override fun onClickFilterDate() {
                presenter.onClickFilterDate()
            }

            override fun onClickFilterStatus() {
                presenter.onClickFilterStatus()
            }
        })
    }

    override fun setEmptySubtitle() {
        toolbar.subtitle = ""
    }

    override fun setDateRangeSubtitle(initialDate: Calendar, finalDate: Calendar) {
        activity?.run {
            val initial = initialDate.toShortDateView(this)
            val final = finalDate.toShortDateView(this)
            toolbar.subtitle = "$initial - $final"
        }
    }

    override fun updateHistoryItem(position: Int) {
        recycler.adapter?.notifyItemChanged(position)
    }

    override fun updateAllItems() {
        recycler.adapter?.notifyDataSetChanged()
    }

    override fun scrollTo(position: Int) {
        activity?.run { linearLayoutManager.scrollPostionToMiddleScreen(this, position) }
    }

    override fun removeHistoryItem(position: Int) {
        recycler.adapter?.notifyItemRemoved(position)
    }

    override fun scrollToTop() = linearLayoutManager.scrollToTop()

    override fun getTaskId(): Long {
        return arguments?.getTaskId() ?: EntityHelper.NO_ID
    }

    override fun hideHistory() {
        recycler.gone()
    }

    override fun showHistory() {
        recycler.visible()
    }

    override fun showEmptyView() {
        ept_view.visible()
        appbar.setExpanded(true)
    }

    override fun hideEmptyView() {
        ept_view.gone()
    }

    override fun setDateFilterSubtitle(dateRangeType: DateFilterTaskHistorySelectableItem) {
        toolbar.subtitle = getString(dateRangeType.getName()).toUpperCase(Locale.getDefault())
    }

    override fun setDoneTasks(doneTasks: String) {
        tvMetricsDone.text = doneTasks
    }

    override fun setNotDoneTasks(notDoneTasks: String) {
        tvMetricsNotDone!!.text = notDoneTasks
    }

    override fun showDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        DateRangeSelectorDialog.show(childFragmentManager, initialDate, finalDate, object: DateRangeSelectorDialog.Callback {
            override fun onClickOk(initialDate: Calendar, finalDate: Calendar) {
                presenter.onClickOkDateRangeSelectorDialog(initialDate, finalDate)
            }
        })
    }

    override fun showStatusFilterDialog(status: TaskStatusSelectableItem) {
        SelectItemDialog.show(childFragmentManager, getString(R.string.filter_status), TaskStatusSelectableItem.getItems(), status, object : SelectItemDialog.Callback {
            override fun onClickItem(item: SelectableItem) {
                presenter.onClickFilterStatusItem(item as TaskStatusSelectableItem)
            }
        })
    }

    override fun showDateFilterDialog(selectedFilter: DateFilterTaskHistorySelectableItem) {
        SelectItemDialog.show(childFragmentManager, getString(R.string.filter), DateFilterTaskHistorySelectableItem.getItems(), selectedFilter, object : SelectItemDialog.Callback {
            override fun onClickItem(item: SelectableItem) {
                onClickFilterDate(item)
            }
        })
    }

    override fun showConfirmationClearAllHistory() {
        val dialog = TwoOptionsDialog()
        dialog.message = getString(R.string.msg_ask_clear_all_history)
        dialog.positiveMsg = R.string.clear
        dialog.positiveCallback = object: TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                presenter.onClickYesClearAllHistory()
            }
        }
        dialog.show(childFragmentManager, "CLEAR_HISTORY")
    }

    override fun showDeleteItemDialog(position: Int) {
        DialogHelper().showDeleteDialog(childFragmentManager, activity!!, object: TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                presenter.onClickYesDeleteHistory(position)
            }
        })
    }

    override fun startInsertTaskActivity(id: Long) {
        InsertTaskActivity.startToEdit(this, id)
    }

    private fun onClickFilterDate(item: SelectableItem) {
        presenter.onClickFilterDate(item as DateFilterTaskHistorySelectableItem)
    }

}