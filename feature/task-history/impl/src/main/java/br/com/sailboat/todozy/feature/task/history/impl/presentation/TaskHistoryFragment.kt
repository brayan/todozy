package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.feature.task.history.impl.R
import br.com.sailboat.todozy.feature.task.history.impl.databinding.FrgTaskHistoryBinding
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.TaskHistoryFilterDialog
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.date.DateFilterDialog
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.date.StatusFilterDialog
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterDialog
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.delete.DeleteTaskHistoryDialog
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickClearAllHistory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickConfirmClearAllHistory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickConfirmDeleteTaskHistory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickDateFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickDeleteTaskHistoryItem
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickMarkTaskAsDone
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickMarkTaskAsNotDone
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickStatusFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnClickTaskHistory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnSelectDateFromFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnSelectDateRange
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnSelectStatusFromFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnStart
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewAction.OnSubmitSearchTerm
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewModel
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.NavigateToClearAllHistoryConfirmation
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.NavigateToDateFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.NavigateToDateRangeFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.NavigateToDeleteTaskHistoryConfirmation
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.NavigateToMenuFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.NavigateToStatusFilter
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.RefreshHistoryItem
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.ScrollToPosition
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.ScrollToTop
import br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel.TaskHistoryViewState.Action.ShowGenericError
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.scrollPositionToMiddleScreen
import br.com.sailboat.todozy.utility.android.view.scrollToTop
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.uicomponent.impl.dialog.selectable.SelectItemDialog
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.uicomponent.impl.dialog.twooptions.TwoOptionsDialog
import br.com.sailboat.uicomponent.impl.helper.putTaskId
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

internal class TaskHistoryFragment : BaseFragment() {

    private val viewModel: TaskHistoryViewModel by viewModel()

    private val linearLayoutManager by lazy {
        binding.rvTaskHistory.layoutManager as LinearLayoutManager
    }

    private var taskHistoryAdapter: TaskHistoryAdapter? = null
    private var taskHistoryFilterDialog: TaskHistoryFilterDialog? = null
    private var dateFilterDialog: SelectItemDialog? = null
    private var dateRangeSelectorFilterDialog: DateRangeSelectorFilterDialog? = null
    private var statusFilterDialog: SelectItemDialog? = null
    private var deleteTaskHistoryDialog: DeleteTaskHistoryDialog? = null
    private var clearAllHistoryDialog: TwoOptionsDialog? = null

    private lateinit var binding: FrgTaskHistoryBinding

    private val taskHistoryFilterDialogCallback = object : TaskHistoryFilterDialog.Callback {
        override fun onClickFilterDate() {
            viewModel.dispatchViewIntent(OnClickDateFilter)
        }

        override fun onClickFilterStatus() {
            viewModel.dispatchViewIntent(OnClickStatusFilter)
        }
    }

    private val dateFilterDialogCallback = object : SelectItemDialog.Callback {
        override fun onClickItem(item: SelectableItem) {
            val date = item as DateFilterTaskHistorySelectableItem
            viewModel.dispatchViewIntent(OnSelectDateFromFilter(date))
        }
    }

    private val statusFilterDialogCallback = object : SelectItemDialog.Callback {
        override fun onClickItem(item: SelectableItem) {
            val status = item as TaskStatusSelectableItem
            viewModel.dispatchViewIntent(OnSelectStatusFromFilter(status))
        }
    }

    private val dateRangeSelectorDialogCallback = object : DateRangeSelectorFilterDialog.Callback {
        override fun onClickOk(initialDate: Calendar, finalDate: Calendar) {
            viewModel.dispatchViewIntent(OnSelectDateRange(initialDate, finalDate))
        }
    }

    private val clearAllHistoryCallback = object : TwoOptionsDialog.Callback {
        override fun onClickPositiveOption() {
            viewModel.dispatchViewIntent(OnClickConfirmClearAllHistory)
        }

        override fun onClickNegativeOption() {}
    }

    private val deleteTaskHistoryCallback = object : DeleteTaskHistoryDialog.Callback {
        override fun onConfirmDeleteTaskHistory(position: Int) {
            viewModel.dispatchViewIntent(OnClickConfirmDeleteTaskHistory(position))
        }
    }

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
        viewModel.dispatchViewIntent(OnStart)
    }

    override fun onResume() {
        super.onResume()
        updateCallbacksFromDialogs()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_history_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                viewModel.dispatchViewIntent(OnClickFilter)
            }
            R.id.menu_clear_history -> {
                viewModel.dispatchViewIntent(OnClickClearAllHistory)
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
        viewModel.dispatchViewIntent(OnSubmitSearchTerm(search))
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressTaskHistory.visible()
                binding.rvTaskHistory.gone()
            } else {
                binding.progressTaskHistory.gone()
                binding.rvTaskHistory.visible()
            }
        }
        viewModel.viewState.taskHistoryList.observe(viewLifecycleOwner) { taskHistoryList ->
            taskHistoryAdapter?.submitList(taskHistoryList)

            if (taskHistoryList.isEmpty()) {
                binding.rvTaskHistory.gone()
                showEmptyView()
            } else {
                binding.rvTaskHistory.visible()
                hideEmptyView()
            }
        }

        viewModel.viewState.taskMetrics.observe(viewLifecycleOwner) { taskMetrics ->
            binding.appbarTaskHistory.taskMetrics.tvMetricsDone.text = taskMetrics.doneTasks.toString()
            binding.appbarTaskHistory.taskMetrics.tvMetricsNotDone.text = taskMetrics.notDoneTasks.toString()
            binding.appbarTaskHistory.taskMetrics.tvMetricsFire.text = taskMetrics.consecutiveDone.toString()

            if (taskMetrics.consecutiveDone == 0) {
                binding.appbarTaskHistory.taskMetrics.taskMetricsLlFire.gone()
            } else {
                binding.appbarTaskHistory.taskMetrics.taskMetricsLlFire.visible()
            }
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

    private fun updateCallbacksFromDialogs() {
        taskHistoryFilterDialog =
            childFragmentManager.findFragmentByTag(TaskHistoryFilterDialog.TAG) as? TaskHistoryFilterDialog
        taskHistoryFilterDialog?.callback = taskHistoryFilterDialogCallback

        dateFilterDialog = childFragmentManager.findFragmentByTag(DateFilterDialog.TAG) as? SelectItemDialog
        dateFilterDialog?.callback = dateFilterDialogCallback

        statusFilterDialog = childFragmentManager.findFragmentByTag(StatusFilterDialog.TAG) as? SelectItemDialog
        statusFilterDialog?.callback = statusFilterDialogCallback

        dateRangeSelectorFilterDialog =
            childFragmentManager.findFragmentByTag(DateRangeSelectorFilterDialog.TAG) as? DateRangeSelectorFilterDialog
        dateRangeSelectorFilterDialog?.callback = dateRangeSelectorDialogCallback

        clearAllHistoryDialog = childFragmentManager.findFragmentByTag("CLEAR_HISTORY") as? TwoOptionsDialog
        clearAllHistoryDialog?.callback = clearAllHistoryCallback

        deleteTaskHistoryDialog =
            childFragmentManager.findFragmentByTag(DeleteTaskHistoryDialog.TAG) as? DeleteTaskHistoryDialog
        deleteTaskHistoryDialog?.callback = deleteTaskHistoryCallback
    }

    private fun navigateToMenuFilter(action: NavigateToMenuFilter) {
        taskHistoryFilterDialog = TaskHistoryFilterDialog.show(
            childFragmentManager,
            action.dateRangeType,
            action.status,
            taskHistoryFilterDialogCallback,
        )
    }

    private fun navigateToDateFilter(action: NavigateToDateFilter) {
        dateFilterDialog = DateFilterDialog.show(
            childFragmentManager,
            getString(R.string.filter),
            DateFilterTaskHistorySelectableItem.getItems(),
            action.dateFilterType,
            dateFilterDialogCallback,
        )
    }

    private fun navigateToDateRangeFilter(action: NavigateToDateRangeFilter) {
        dateRangeSelectorFilterDialog = DateRangeSelectorFilterDialog.show(
            childFragmentManager,
            action.initialDate,
            action.finalDate,
            dateRangeSelectorDialogCallback,
        )
    }

    private fun navigateToStatusFilter(action: NavigateToStatusFilter) {
        statusFilterDialog = StatusFilterDialog.show(
            childFragmentManager,
            getString(R.string.filter_status),
            TaskStatusSelectableItem.getItems(),
            action.status,
            statusFilterDialogCallback,
        )
    }

    private fun navigateToClearAllHistoryConfirmation() {
        clearAllHistoryDialog = TwoOptionsDialog.newInstance(
            message = getString(R.string.msg_ask_clear_all_history),
            positiveMsg = R.string.clear,
        )
        clearAllHistoryDialog?.callback = clearAllHistoryCallback
        clearAllHistoryDialog?.show(childFragmentManager, "CLEAR_HISTORY")
    }

    private fun navigateToDeleteTaskHistoryConfirmation(action: NavigateToDeleteTaskHistoryConfirmation) {
        deleteTaskHistoryDialog = DeleteTaskHistoryDialog.newInstance(taskHistoryPosition = action.position)
        deleteTaskHistoryDialog?.callback = deleteTaskHistoryCallback
        deleteTaskHistoryDialog?.show(childFragmentManager, DeleteTaskHistoryDialog.TAG)
    }

    private fun refreshHistoryItem(action: RefreshHistoryItem) {
        binding.rvTaskHistory.adapter?.notifyItemChanged(action.position)
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
        binding.rvTaskHistory.run {
            adapter = TaskHistoryAdapter(object : TaskHistoryAdapter.Callback {
                override fun onClickMarkTaskAsDone(position: Int) {
                    viewModel.dispatchViewIntent(OnClickMarkTaskAsDone(position))
                }

                override fun onClickMarkTaskAsNotDone(position: Int) {
                    viewModel.dispatchViewIntent(OnClickMarkTaskAsNotDone(position))
                }

                override fun onClickHistory(position: Int) {
                    viewModel.dispatchViewIntent(OnClickTaskHistory(position))
                }

                override fun isShowingOptions(position: Int) = viewModel.isShowingOptions(position)

                override fun onClickDelete(position: Int) {
                    viewModel.dispatchViewIntent(OnClickDeleteTaskHistoryItem(position))
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

    private fun showEmptyView() {
        binding.eptView.root.visible()
        binding.appbarTaskHistory.root.setExpanded(true)
    }

    private fun hideEmptyView() {
        binding.eptView.root.gone()
    }
}
