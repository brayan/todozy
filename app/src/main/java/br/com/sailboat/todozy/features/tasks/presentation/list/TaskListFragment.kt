package br.com.sailboat.todozy.features.tasks.presentation.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.databinding.FrgTaskListBinding
import br.com.sailboat.todozy.feature.about.presentation.AboutHelper
import br.com.sailboat.todozy.feature.about.presentation.startAboutActivity
import br.com.sailboat.todozy.features.settings.presentation.startSettingsActivity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.presentation.details.startTaskDetailsActivity
import br.com.sailboat.todozy.features.tasks.presentation.form.startTaskFormActivity
import br.com.sailboat.todozy.features.tasks.presentation.history.startTaskHistoryActivity
import br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel.TaskListViewAction
import br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel.TaskListViewModel
import br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel.TaskListViewState.Action.*
import br.com.sailboat.todozy.uicomponent.helper.NotificationHelper
import br.com.sailboat.todozy.uicomponent.helper.SwipeTaskLeftRight
import br.com.sailboat.todozy.utility.android.dialog.ProgressDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.log.logDebug
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.hideFabWhenScrolling
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment : BaseFragment() {

    private val viewModel: TaskListViewModel by viewModel()

    private lateinit var binding: FrgTaskListBinding
    private var progress: ProgressDialog? = null
    private var taskListAdapter: TaskListAdapter? = null

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
        initMenusVisibility(menu)
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
            R.id.menu_fragments_about -> {
                viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuAbout)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.dispatchViewAction(TaskListViewAction.OnStart)
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
            }
        }
    }

    override fun onSubmitSearch(search: String) {
        viewModel.dispatchViewAction(TaskListViewAction.OnInputSearchTerm(term = search))
        "onSubmitSearch: $search".logDebug()
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
        startTaskFormActivity()
    }

    private fun navigateToSettings() {
        startSettingsActivity()
    }

    private fun showTasks() {
        binding.recycler.visible()
    }

    private fun navigateToTaskDetails(taskId: Long) {
        startTaskDetailsActivity(taskId)
    }

    private fun navigateToHistory() {
        activity?.startTaskHistoryActivity()
    }

    private fun updateRemovedTask(position: Int) {
        taskListAdapter?.notifyItemRemoved(position)
    }

    private fun navigateToAbout() {
        activity?.run { startAboutActivity(AboutHelper(this).getInfo()) }
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

    private fun initMenusVisibility(menu: Menu) {
        val settings = menu.findItem(R.id.menu_fragments_settings)
        val about = menu.findItem(R.id.menu_fragments_about)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            settings.isVisible = true
            about.isVisible = false
        } else {
            settings.isVisible = false
            about.isVisible = true
        }
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