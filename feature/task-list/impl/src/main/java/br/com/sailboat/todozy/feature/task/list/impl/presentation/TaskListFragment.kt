package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator
import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskDetailsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.todozy.feature.task.list.impl.databinding.FrgTaskListBinding
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewModel
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.hideFabWhenScrolling
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.uicomponent.impl.helper.NotificationHelper
import br.com.sailboat.uicomponent.impl.helper.SwipeTaskLeftRight
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TaskListFragment : BaseFragment() {

    private val viewModel: TaskListViewModel by viewModel()
    private val taskDetailsNavigator: TaskDetailsNavigator by inject()
    private val taskHistoryNavigator: TaskHistoryNavigator by inject()
    private val taskFormNavigator: TaskFormNavigator by inject()
    private val aboutNavigator: AboutNavigator by inject()
    private val settingsNavigator: SettingsNavigator by inject()

    private lateinit var binding: FrgTaskListBinding
    private var taskListAdapter: TaskListAdapter? = null

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FrgTaskListBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fragments_history -> {
                viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuHistory)
            }
            R.id.menu_fragments_settings -> {
                viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuSettings)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
    }

    override fun initViews() {
        binding.toolbar.setTitle(R.string.app_name)

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        binding.eptView.tvEmptyViewMessagePrimary.setText(R.string.no_tasks)
        binding.eptView.tvEmptyViewMessageSecondary.setText(R.string.ept_click_to_add)

        initRecyclerView()

        binding.fab.setOnClickListener {
            viewModel.dispatchViewIntent(TaskListViewIntent.OnClickNewTask)
        }
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressTaskList.visible()
                binding.rvTaskList.gone()
            } else {
                binding.progressTaskList.gone()
                binding.rvTaskList.visible()
            }
        }
        viewModel.viewState.itemsView.observe(viewLifecycleOwner) { items ->
            taskListAdapter?.submitList(items)

            if (items.isEmpty()) {
                binding.rvTaskList.gone()
                showEmptyView()
            } else {
                binding.rvTaskList.visible()
                hideEmptyView()
            }
        }
        viewModel.viewState.taskMetrics.observe(viewLifecycleOwner) { taskMetrics ->
            taskMetrics?.run { showMetrics(this) } ?: hideMetrics()
        }
    }

    private fun observeActions() {
        viewModel.viewState.viewAction.observe(viewLifecycleOwner) { viewAction ->
            when (viewAction) {
                is TaskListViewAction.CloseNotifications -> closeNotifications()
                is TaskListViewAction.NavigateToAbout -> navigateToAbout()
                is TaskListViewAction.NavigateToHistory -> navigateToHistory()
                is TaskListViewAction.NavigateToSettings -> navigateToSettings()
                is TaskListViewAction.NavigateToTaskForm -> navigateToTaskForm()
                is TaskListViewAction.NavigateToTaskDetails -> navigateToTaskDetails(viewAction.taskId)
                is TaskListViewAction.UpdateRemovedTask -> updateRemovedTask(viewAction.position)
                is TaskListViewAction.ShowErrorCompletingTask -> showErrorCompletingTask()
                is TaskListViewAction.ShowErrorLoadingTasks -> showErrorLoadingTasks()
            }
        }
    }

    override fun onSubmitSearch(search: String) {
        viewModel.dispatchViewIntent(TaskListViewIntent.OnSubmitSearchTerm(term = search))
    }

    private fun closeNotifications() {
        activity?.apply { NotificationHelper().closeNotifications(this) }
    }

    private fun hideEmptyView() {
        binding.eptView.root.gone()
    }

    private fun hideMetrics() {
        binding.appbarTaskListFlMetrics.gone()
    }

    private fun showEmptyView() {
        binding.eptView.root.visible()
    }

    private fun showMetrics(taskMetrics: TaskMetrics) {
        binding.taskMetrics.tvMetricsFire.text = taskMetrics.consecutiveDone.toString()
        binding.taskMetrics.tvMetricsDone.text = taskMetrics.doneTasks.toString()
        binding.taskMetrics.tvMetricsNotDone.text = taskMetrics.notDoneTasks.toString()

        if (taskMetrics.consecutiveDone == 0) {
            binding.taskMetrics.taskMetricsLlFire.gone()
        } else {
            binding.taskMetrics.taskMetricsLlFire.visible()
        }

        binding.appbar.setExpanded(true, true)
        binding.appbarTaskListFlMetrics.visible()
    }

    private fun navigateToTaskForm() {
        taskFormNavigator.navigateToAddTask(requireContext(), launcher)
    }

    private fun navigateToSettings() {
        settingsNavigator.navigateToSettings(requireContext(), launcher)
    }

    private fun navigateToTaskDetails(taskId: Long) {
        taskDetailsNavigator.navigateToTaskDetails(requireContext(), taskId, launcher)
    }

    private fun navigateToHistory() {
        taskHistoryNavigator.navigateToTaskHistory(requireContext())
    }

    private fun updateRemovedTask(position: Int) {
        taskListAdapter?.notifyItemRemoved(position)
    }

    private fun showErrorLoadingTasks() {
        Toast.makeText(activity, R.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorCompletingTask() {
        Toast.makeText(activity, R.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAbout() {
        activity?.run { aboutNavigator.navigateToAbout(this) }
    }

    private fun initRecyclerView() {
        binding.rvTaskList.run {
            adapter = TaskListAdapter(object : TaskListAdapter.Callback {
                override fun onClickTask(taskId: Long) {
                    viewModel.dispatchViewIntent(TaskListViewIntent.OnClickTask(taskId = taskId))
                }
            }).apply {
                taskListAdapter = this
            }
            layoutManager = LinearLayoutManager(activity)
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeTaskLeftRight(
                requireContext(),
                object : SwipeTaskLeftRight.Callback {
                    override fun onSwipeLeft(position: Int) {
                        viewModel.dispatchViewIntent(
                            TaskListViewIntent.OnSwipeTask(
                                position = position,
                                status = TaskStatus.NOT_DONE
                            )
                        )
                    }

                    override fun onSwipeRight(position: Int) {
                        viewModel.dispatchViewIntent(
                            TaskListViewIntent.OnSwipeTask(
                                position = position,
                                status = TaskStatus.DONE
                            )
                        )
                    }
                }
            )
        )

        itemTouchHelper.attachToRecyclerView(binding.rvTaskList)

        binding.rvTaskList.hideFabWhenScrolling(binding.fab)
    }
}
