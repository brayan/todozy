package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.feature.task.history.impl.R
import br.com.sailboat.todozy.feature.task.history.impl.databinding.FrgTaskHistoryBinding
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.*
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewModel
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.*
import br.com.sailboat.todozy.uicomponent.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.uicomponent.dialog.selectable.SelectItemDialog
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.SelectableItem
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.todozy.uicomponent.helper.DialogHelper
import br.com.sailboat.todozy.uicomponent.helper.putTaskId
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.scrollPositionToMiddleScreen
import br.com.sailboat.todozy.utility.android.view.scrollToTop
import br.com.sailboat.todozy.utility.android.view.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TaskHistoryFragment : BaseFragment() {

    private val viewModel: TaskHistoryViewModel by viewModel()

    private val linearLayoutManager by lazy {
        binding.recycler.layoutManager as LinearLayoutManager
    }

    private var taskHistoryAdapter: TaskHistoryAdapter? = null

    private lateinit var binding: FrgTaskHistoryBinding

    companion object {
        fun newInstance() = TaskHistoryFragment()

        fun newInstance(taskId: Long): TaskHistoryFragment = with(TaskHistoryFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FrgTaskHistoryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.dispatchViewAction(OnStart)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_history_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                viewModel.dispatchViewAction(OnClickFilter)
            }
            R.id.menu_clear_history -> {
                viewModel.dispatchViewAction(OnClickClearAllHistory)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun initViews() {
        initToolbar()
        initRecyclerView()
        initEmptyState()
    }

    override fun onSubmitSearch(search: String) {
        viewModel.dispatchViewAction(OnSubmitSearchTerm(search))
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.taskHistoryList.observe(viewLifecycleOwner) { taskHistoryList ->
            taskHistoryAdapter?.submitList(taskHistoryList)

            if (taskHistoryList.isEmpty()) {
                hideHistoryList()
                showEmptyView()
            } else {
                showHistoryList()
                hideEmptyView()
            }
        }

        viewModel.viewState.taskMetrics.observe(viewLifecycleOwner) { taskMetrics ->
            binding.appbarTaskHistory.taskMetrics.tvMetricsDone.text =
                taskMetrics.doneTasks.toString()
            binding.appbarTaskHistory.taskMetrics.tvMetricsNotDone.text =
                taskMetrics.notDoneTasks.toString()
        }

        viewModel.viewState.subtitle.observe(viewLifecycleOwner) { subtitle ->
            binding.appbarTaskHistory.toolbarScroll.toolbar.subtitle = subtitle
        }
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is NavigateToMenuFilter -> navigateToMenuFilter(action)
                is NavigateToDateFilter -> navigateToDateFilter(action)
                is NavigateToDateRangeFilter -> navigateToDateRangeFilter(action)
                is NavigateToStatusFilter -> navigateToStatusFilter(action)
                is NavigateToClearAllHistoryConfirmation -> navigateToClearAllHistoryConfirmation()
                is NavigateToDeleteTaskHistoryConfirmation ->
                    navigateToDeleteTaskHistoryConfirmation(action)
                is RefreshHistoryItem -> refreshHistoryItem(action)
                is ScrollToTop -> scrollToTop()
                is ScrollToPosition -> scrollToPosition(action)
                is ShowGenericError -> showGenericError()
            }
        }
    }

    private fun navigateToMenuFilter(action: NavigateToMenuFilter) {
        TaskHistoryFilterDialog.show(
            childFragmentManager,
            action.dateRangeType,
            action.status,
            object : TaskHistoryFilterDialog.Callback {
                override fun onClickFilterDate() {
                    viewModel.dispatchViewAction(OnClickDateFilter)
                }

                override fun onClickFilterStatus() {
                    viewModel.dispatchViewAction(OnClickStatusFilter)
                }
            })
    }

    private fun navigateToDateFilter(action: NavigateToDateFilter) {
        SelectItemDialog.show(
            childFragmentManager,
            getString(R.string.filter),
            DateFilterTaskHistorySelectableItem.getItems(),
            action.dateFilterType,
            object : SelectItemDialog.Callback {
                override fun onClickItem(item: SelectableItem) {
                    val date = item as DateFilterTaskHistorySelectableItem
                    viewModel.dispatchViewAction(OnSelectDateFromFilter(date))
                }
            })
    }

    private fun navigateToDateRangeFilter(action: NavigateToDateRangeFilter) {
        DateRangeSelectorDialog.show(
            childFragmentManager,
            action.initialDate,
            action.finalDate,
            object : DateRangeSelectorDialog.Callback {
                override fun onClickOk(initialDate: Calendar, finalDate: Calendar) {
                    viewModel.dispatchViewAction(OnSelectDateRange(initialDate, finalDate))
                }
            })
    }

    private fun navigateToStatusFilter(action: NavigateToStatusFilter) {
        SelectItemDialog.show(
            childFragmentManager,
            getString(R.string.filter_status),
            TaskStatusSelectableItem.getItems(),
            action.status,
            object : SelectItemDialog.Callback {
                override fun onClickItem(item: SelectableItem) {
                    val status = item as TaskStatusSelectableItem
                    viewModel.dispatchViewAction(OnSelectStatusFromFilter(status))
                }
            })
    }

    private fun navigateToClearAllHistoryConfirmation() {
        val dialog = TwoOptionsDialog()
        dialog.message = getString(R.string.msg_ask_clear_all_history)
        dialog.positiveMsg = R.string.clear
        dialog.positiveCallback = object : TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                viewModel.dispatchViewAction(OnClickConfirmClearAllHistory)
            }
        }
        dialog.show(childFragmentManager, "CLEAR_HISTORY")
    }

    private fun navigateToDeleteTaskHistoryConfirmation(action: NavigateToDeleteTaskHistoryConfirmation) {
        activity?.run {
            DialogHelper().showDeleteDialog(
                childFragmentManager,
                this,
                object : TwoOptionsDialog.PositiveCallback {
                    override fun onClickPositiveOption() {
                        viewModel.dispatchViewAction(OnClickConfirmDeleteTaskHistory(action.position))
                    }
                })
        }
    }

    private fun refreshHistoryItem(action: RefreshHistoryItem) {
        binding.recycler.adapter?.notifyItemChanged(action.position)
    }

    private fun scrollToTop() {
        linearLayoutManager.scrollToTop()
    }

    private fun scrollToPosition(action: ScrollToPosition) {
        activity?.run {
            linearLayoutManager.scrollPositionToMiddleScreen(this, action.position)
        }
    }

    private fun showGenericError() {
        Toast.makeText(activity, R.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun initEmptyState() {
        binding.eptView.tvEmptyViewMessagePrimary.setText(R.string.no_history_found)
    }

    private fun initRecyclerView() {
        binding.recycler.run {
            adapter = TaskHistoryAdapter(object : TaskHistoryAdapter.Callback {
                override fun onClickMarkTaskAsDone(position: Int) {
                    viewModel.dispatchViewAction(OnClickMarkTaskAsDone(position))
                }

                override fun onClickMarkTaskAsNotDone(position: Int) {
                    viewModel.dispatchViewAction(OnClickMarkTaskAsNotDone(position))
                }

                override fun onClickHistory(position: Int) {
                    viewModel.dispatchViewAction(OnClickTaskHistory(position))
                }

                override fun isShowingOptions(position: Int) = viewModel.isShowingOptions(position)

                override fun onClickDelete(position: Int) {
                    viewModel.dispatchViewAction(OnClickDeleteTaskHistoryItem(position))
                }
            }).apply {
                taskHistoryAdapter = this
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun initToolbar() {
        val toolbar = binding.appbarTaskHistory.toolbarScroll.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.setTitle(R.string.history)
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

    private fun hideHistoryList() {
        binding.recycler.gone()
    }

    private fun showHistoryList() {
        binding.recycler.visible()
    }

    private fun showEmptyView() {
        binding.eptView.root.visible()
        binding.appbarTaskHistory.root.setExpanded(true)
    }

    private fun hideEmptyView() {
        binding.eptView.root.gone()
    }

    fun setDateFilterSubtitle(dateRangeType: DateFilterTaskHistorySelectableItem) {
        binding.appbarTaskHistory.toolbarScroll.toolbar.subtitle =
            getString(dateRangeType.getName()).uppercase()
    }

}