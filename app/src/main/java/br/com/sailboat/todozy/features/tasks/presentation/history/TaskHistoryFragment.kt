package br.com.sailboat.todozy.features.tasks.presentation.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.extensions.scrollPostionToMiddleScreen
import br.com.sailboat.todozy.core.extensions.scrollToTop
import br.com.sailboat.todozy.core.presentation.base.BaseFragment
import br.com.sailboat.todozy.core.presentation.dialog.DateRangeSelectorDialog
import br.com.sailboat.todozy.core.presentation.dialog.TaskHistoryFilterDialog
import br.com.sailboat.todozy.core.presentation.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.selectable.SelectItemDialog
import br.com.sailboat.todozy.core.presentation.dialog.selectable.SelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.features.tasks.presentation.form.startTaskFormActivity
import kotlinx.android.synthetic.main.appbar_task_history.*
import kotlinx.android.synthetic.main.ept_view.*
import kotlinx.android.synthetic.main.frg_task_history.*
import kotlinx.android.synthetic.main.task_metrics.*
import kotlinx.android.synthetic.main.toolbar_scroll.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TaskHistoryFragment : BaseFragment() {

    //    override val presenter: TaskHistoryContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_history

    val viewModel: TaskHistoryViewModel by viewModel() // or sharedViewModel?

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
            R.id.menu_filter -> viewModel.onClickMenuFilter()
            R.id.menu_clear_history -> viewModel.onClickCleanAllHistory()
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
                override fun onClickMarkTaskAsDone(position: Int) = viewModel.onClickMarkTaskAsDone(position)
                override fun onClickMarkTaskAsNotDone(position: Int) = viewModel.onClickMarkTaskAsNotDone(position)
                override fun onClickHistory(position: Int) = viewModel.onClickHistory(position)
                override fun isShowingOptions(position: Int) = viewModel.isShowingOptions(position)
                override fun onClickDelete(position: Int) = viewModel.onClickDelete(position)
                override val history: List<ItemView> = viewModel.history
            })
            layoutManager = LinearLayoutManager(activity)
        }

        tvEmptyViewMessagePrimary.setText(R.string.no_history_found)
    }

    override fun onResume() {
        super.onResume()
        initObservers()
        viewModel.start()
    }

    override fun onSubmitSearch(search: String) {
        viewModel.onSubmitSearch(search)
    }

    fun showFilterDialog(dateRangeType: DateFilterTaskHistorySelectableItem, status: TaskStatusSelectableItem) {
        TaskHistoryFilterDialog.show(childFragmentManager, dateRangeType, status, object : TaskHistoryFilterDialog.Callback {
            override fun onClickFilterDate() {
                viewModel.onClickFilterDate()
            }

            override fun onClickFilterStatus() {
                viewModel.onClickFilterStatus()
            }
        })
    }

    fun setEmptySubtitle() {
        toolbar.subtitle = ""
    }

    fun setDateRangeSubtitle(initialDate: Calendar, finalDate: Calendar) {
        activity?.run {
            val initial = initialDate.toShortDateView(this)
            val final = finalDate.toShortDateView(this)
            toolbar.subtitle = "$initial - $final"
        }
    }

    fun updateHistoryItem(position: Int) {
        recycler.adapter?.notifyItemChanged(position)
    }

    fun updateAllItems() {
        recycler.adapter?.notifyDataSetChanged()
    }

    fun scrollTo(position: Int) {
        activity?.run { linearLayoutManager.scrollPostionToMiddleScreen(this, position) }
    }

    fun removeHistoryItem(position: Int) {
        recycler.adapter?.notifyItemRemoved(position)
    }

    fun scrollToTop() = linearLayoutManager.scrollToTop()

    fun getTaskId(): Long {
        return arguments?.getTaskId() ?: Entity.NO_ID
    }

    fun hideHistory() {
        recycler.gone()
    }

    fun showHistory() {
        recycler.visible()
    }

    fun showEmptyView() {
        ept_view.visible()
        appbar.setExpanded(true)
    }

    fun hideEmptyView() {
        ept_view.gone()
    }

    fun setDateFilterSubtitle(dateRangeType: DateFilterTaskHistorySelectableItem) {
        toolbar.subtitle = getString(dateRangeType.getName()).toUpperCase(Locale.getDefault())
    }

    fun setDoneTasks(doneTasks: String) {
        tvMetricsDone.text = doneTasks
    }

    fun setNotDoneTasks(notDoneTasks: String) {
        tvMetricsNotDone!!.text = notDoneTasks
    }

    fun showDateRangeSelectorDialog(initialDate: Calendar, finalDate: Calendar) {
        DateRangeSelectorDialog.show(childFragmentManager, initialDate, finalDate, object : DateRangeSelectorDialog.Callback {
            override fun onClickOk(initialDate: Calendar, finalDate: Calendar) {
                viewModel.onClickOkDateRangeSelectorDialog(initialDate, finalDate)
            }
        })
    }

    fun showStatusFilterDialog(status: TaskStatusSelectableItem) {
        SelectItemDialog.show(childFragmentManager, getString(R.string.filter_status), TaskStatusSelectableItem.getItems(), status, object : SelectItemDialog.Callback {
            override fun onClickItem(item: SelectableItem) {
                viewModel.onClickFilterStatusItem(item as TaskStatusSelectableItem)
            }
        })
    }

    fun showDateFilterDialog(selectedFilter: DateFilterTaskHistorySelectableItem) {
        SelectItemDialog.show(childFragmentManager, getString(R.string.filter), DateFilterTaskHistorySelectableItem.getItems(), selectedFilter, object : SelectItemDialog.Callback {
            override fun onClickItem(item: SelectableItem) {
                onClickFilterDate(item)
            }
        })
    }

    fun showConfirmationClearAllHistory() {
        val dialog = TwoOptionsDialog()
        dialog.message = getString(R.string.msg_ask_clear_all_history)
        dialog.positiveMsg = R.string.clear
        dialog.positiveCallback = object : TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                viewModel.onClickYesClearAllHistory()
            }
        }
        dialog.show(childFragmentManager, "CLEAR_HISTORY")
    }

    fun showDeleteItemDialog(position: Int) {
        DialogHelper().showDeleteDialog(childFragmentManager, activity!!, object : TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                viewModel.onClickYesDeleteHistory(position)
            }
        })
    }

    fun startInsertTaskActivity(id: Long) {
        startTaskFormActivity(id)
    }

    private fun onClickFilterDate(item: SelectableItem) {
        viewModel.onClickFilterDate(item as DateFilterTaskHistorySelectableItem)
    }

    private fun initObservers() {
        viewModel.refreshHistory.observe(viewLifecycleOwner, EventObserver {
            recycler.adapter?.notifyDataSetChanged()
        })
    }

}