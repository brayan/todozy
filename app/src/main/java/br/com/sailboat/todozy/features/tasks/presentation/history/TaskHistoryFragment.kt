package br.com.sailboat.todozy.features.tasks.presentation.history

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
import br.com.sailboat.todozy.databinding.FrgTaskHistoryBinding
import br.com.sailboat.todozy.features.tasks.presentation.form.startTaskFormActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TaskHistoryFragment : BaseFragment() {

    val viewModel: TaskHistoryViewModel by viewModel() // or sharedViewModel?

    private val linearLayoutManager by lazy { binding.recycler.layoutManager as LinearLayoutManager }

    companion object {
        fun newInstance() = TaskHistoryFragment()

        fun newInstance(taskId: Long): TaskHistoryFragment = with(TaskHistoryFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    private lateinit var binding: FrgTaskHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgTaskHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        initToolbar()
        initRecyclerView()
        initEmptyState()
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
        binding.appbarTaskHistory.toolbarScroll.toolbar.subtitle = ""
    }

    fun setDateRangeSubtitle(initialDate: Calendar, finalDate: Calendar) {
        activity?.run {
            val initial = initialDate.toShortDateView(this)
            val final = finalDate.toShortDateView(this)
            binding.appbarTaskHistory.toolbarScroll.toolbar.subtitle = "$initial - $final"
        }
    }

    fun updateHistoryItem(position: Int) {
        binding.recycler.adapter?.notifyItemChanged(position)
    }

    fun updateAllItems() {
        binding.recycler.adapter?.notifyDataSetChanged()
    }

    fun scrollTo(position: Int) {
        activity?.run { linearLayoutManager.scrollPostionToMiddleScreen(this, position) }
    }

    fun removeHistoryItem(position: Int) {
        binding.recycler.adapter?.notifyItemRemoved(position)
    }

    fun scrollToTop() = linearLayoutManager.scrollToTop()

    fun getTaskId(): Long {
        return arguments?.getTaskId() ?: Entity.NO_ID
    }

    fun hideHistory() {
        binding.recycler.gone()
    }

    fun showHistory() {
        binding.recycler.visible()
    }

    fun showEmptyView() {
        binding.eptView.root.visible()
        binding.appbarTaskHistory.root.setExpanded(true)
    }

    fun hideEmptyView() {
        binding.eptView.root.gone()
    }

    fun setDateFilterSubtitle(dateRangeType: DateFilterTaskHistorySelectableItem) {
        binding.appbarTaskHistory.toolbarScroll.toolbar.subtitle = getString(dateRangeType.getName()).toUpperCase(Locale.getDefault())
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

    private fun showDeleteItemDialog(position: Int) {
        activity?.run {
            DialogHelper().showDeleteDialog(childFragmentManager, this, object : TwoOptionsDialog.PositiveCallback {
                override fun onClickPositiveOption() {
                    viewModel.onClickYesDeleteHistory(position)
                }
            })
        }
    }

    fun startInsertTaskActivity(id: Long) {
        startTaskFormActivity(id)
    }

    private fun onClickFilterDate(item: SelectableItem) {
        viewModel.onClickFilterDate(item as DateFilterTaskHistorySelectableItem)
    }

    private fun initObservers() {
        viewModel.refreshHistory.observe(viewLifecycleOwner, EventObserver {
            binding.recycler.adapter?.notifyDataSetChanged()
        })

        viewModel.taskMetrics.observe(viewLifecycleOwner,  { taskMetrics ->
            binding.appbarTaskHistory.taskMetrics.tvMetricsDone.text = taskMetrics.doneTasks.toString()
            binding.appbarTaskHistory.taskMetrics.tvMetricsNotDone.text = taskMetrics.notDoneTasks.toString()
        })

        viewModel.updateHistoryItem.observe(viewLifecycleOwner,  EventObserver { position ->
            binding.recycler.adapter?.notifyItemChanged(position)
        })

        viewModel.showConfirmationDelete.observe(viewLifecycleOwner,  EventObserver { position ->
            showDeleteItemDialog(position)
        })

        viewModel.removeHistoryItem.observe(viewLifecycleOwner,  EventObserver { position ->
            binding.recycler.adapter?.notifyItemRemoved(position)
        })

    }

    private fun initEmptyState() {
        binding.eptView.tvEmptyViewMessagePrimary.setText(R.string.no_history_found)
    }

    private fun initRecyclerView() {
        binding.recycler.run {
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
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.appbarTaskHistory.toolbarScroll.toolbar)
        binding.appbarTaskHistory.toolbarScroll.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        binding.appbarTaskHistory.toolbarScroll.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        binding.appbarTaskHistory.toolbarScroll.toolbar.setTitle(R.string.history)
    }

}