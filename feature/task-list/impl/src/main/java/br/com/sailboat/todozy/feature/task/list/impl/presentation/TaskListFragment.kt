package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.about.presentation.navigator.AboutNavigator
import br.com.sailboat.todozy.feature.settings.presentation.navigator.SettingsNavigator
import br.com.sailboat.todozy.feature.task.details.presentation.navigator.TaskDetailsNavigator
import br.com.sailboat.todozy.feature.task.form.presentation.navigator.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.history.presentation.navigator.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.todozy.feature.task.list.impl.databinding.FrgTaskListBinding
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewModel
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.*
import br.com.sailboat.todozy.uicomponent.helper.NotificationHelper
import br.com.sailboat.todozy.uicomponent.helper.SwipeTaskLeftRight
import br.com.sailboat.todozy.utility.android.dialog.ProgressDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.hideFabWhenScrolling
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment : BaseFragment() {

    private val viewModel: TaskListViewModel by viewModel()
    private val taskDetailsNavigator: TaskDetailsNavigator by inject()
    private val taskHistoryNavigator: TaskHistoryNavigator by inject()
    private val taskFormNavigator: TaskFormNavigator by inject()
    private val aboutNavigator: AboutNavigator by inject()
    private val settingsNavigator: SettingsNavigator by inject()

    private lateinit var binding: FrgTaskListBinding
    private var progress: ProgressDialog? = null
    private var taskListAdapter: TaskListAdapter? = null

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.dispatchViewAction(TaskListViewAction.OnStart)
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
        viewModel.dispatchViewAction(TaskListViewAction.OnStart)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fragments_history -> {
                viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuHistory)
            }
            R.id.menu_fragments_settings -> {
                viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuSettings)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun initViews() {
        binding.toolbar.setTitle(R.string.app_name)

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        binding.eptView.tvEmptyViewMessagePrimary.setText(R.string.no_tasks)
        binding.eptView.tvEmptyViewMessageSecondary.setText(R.string.ept_click_to_add)

        initRecyclerView()

        binding.fab.setOnClickListener {
            viewModel.dispatchViewAction(TaskListViewAction.OnClickNewTask)
        }
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) showProgress() else hideProgress()
        }
        viewModel.viewState.itemsView.observe(viewLifecycleOwner) { items ->
            taskListAdapter?.submitList(items)

            if (items.isEmpty()) {
                hideTasks()
                showEmptyView()
            } else {
                showTasks()
                hideEmptyView()
            }
        }
        viewModel.viewState.taskMetrics.observe(viewLifecycleOwner) { taskMetrics ->
            taskMetrics?.run { showMetrics(this) } ?: hideMetrics()
        }
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is CloseNotifications -> closeNotifications()
                is NavigateToAbout -> navigateToAbout()
                is NavigateToHistory -> navigateToHistory()
                is NavigateToSettings -> navigateToSettings()
                is NavigateToTaskForm -> navigateToTaskForm()
                is NavigateToTaskDetails -> navigateToTaskDetails(action.taskId)
                is UpdateRemovedTask -> updateRemovedTask(action.position)
                is ShowErrorCompletingTask -> showErrorCompletingTask()
                is ShowErrorLoadingTasks -> showErrorLoadingTasks()
            }
        }
    }

    override fun onSubmitSearch(search: String) {
        viewModel.dispatchViewAction(TaskListViewAction.OnSubmitSearchTerm(term = search))
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

    private fun hideTasks() {
        binding.recycler.gone()
    }

    private fun showTasks() {
        binding.recycler.visible()
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
        binding.recycler.run {
            adapter = TaskListAdapter(object : TaskListAdapter.Callback {
                override fun onClickTask(taskId: Long) {
                    viewModel.dispatchViewAction(TaskListViewAction.OnClickTask(taskId = taskId))
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
                        viewModel.dispatchViewAction(
                            TaskListViewAction.OnSwipeTask(
                                position = position,
                                status = TaskStatus.NOT_DONE
                            )
                        )
                    }

                    override fun onSwipeRight(position: Int) {
                        viewModel.dispatchViewAction(
                            TaskListViewAction.OnSwipeTask(
                                position = position,
                                status = TaskStatus.DONE
                            )
                        )
                    }
                })
        )

        itemTouchHelper.attachToRecyclerView(binding.recycler)

        binding.recycler.hideFabWhenScrolling(binding.fab)
    }

    private fun showProgress() {
        progress = ProgressDialog()
        if (progress?.isAdded.isTrue().not()) {
            progress?.show(childFragmentManager, "PROGRESS")
        }
    }

    private fun hideProgress() {
        progress?.dismissAllowingStateLoss()
        progress = null
    }

}