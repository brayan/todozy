package br.com.sailboat.todozy.feature.task.details.impl.presentation

import android.app.Activity
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
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.details.impl.databinding.FrgTaskDetailsBinding
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewAction
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewIntent
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewIntent.OnClickConfirmDeleteTask
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewModel
import br.com.sailboat.todozy.utility.android.fragment.hapticHandled
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.setSafeClickListener
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.dialog.twooptions.TwoOptionsDialog
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.putTaskId
import br.com.sailboat.uicomponent.impl.progress.TaskProgressHeaderAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import br.com.sailboat.todozy.feature.task.details.impl.R as TaskDetailsR
import br.com.sailboat.uicomponent.impl.R as UiR

private const val DELETE_TASK_TAG = "DELETE_TASK_TAG"

internal class TaskDetailsFragment : Fragment() {
    private val viewModel: TaskDetailsViewModel by viewModel()
    private val taskFormNavigator: TaskFormNavigator by inject()
    private val deleteTaskCallback =
        object : TwoOptionsDialog.Callback {
            override fun onClickPositiveOption() {
                viewModel.dispatchViewIntent(OnClickConfirmDeleteTask)
            }

            override fun onClickNegativeOption() {}
        }
    private var taskDetailsAdapter: TaskDetailsAdapter? = null
    private lateinit var progressAdapter: TaskProgressHeaderAdapter
    private var deleteTaskDialog: TwoOptionsDialog? = null

    private val editTaskLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnReturnToDetails)
        }

    companion object {
        fun newInstance(taskId: Long): TaskDetailsFragment =
            with(TaskDetailsFragment()) {
                val bundle = Bundle()
                bundle.putTaskId(taskId)
                arguments = bundle
                return this
            }
    }

    private lateinit var binding: FrgTaskDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FrgTaskDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        updateCallbacksFromDialogs()

        val taskId = arguments?.getTaskId() ?: Entity.NO_ID
        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
    }

    private fun initViews() {
        binding.toolbar.setTitle(UiR.string.task_details)
        val fabLabel = getString(TaskDetailsR.string.fab_edit_task)
        binding.fab.root.text = fabLabel
        binding.fab.root.setIconResource(UiR.drawable.ic_edit_white_24dp)
        binding.fab.root.contentDescription = fabLabel
        TooltipCompat.setTooltipText(binding.fab.root, fabLabel)
        binding.fab.root.setSafeClickListener {
            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickEditTask)
        }

        binding.rvTaskDetails.run {
            progressAdapter =
                TaskProgressHeaderAdapter(
                    onRangeSelected = { selectedRange ->
                        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnSelectProgressRange(selectedRange))
                    },
                    onDayClick = { /* tooltip handled inside component */ },
                )
            val detailsAdapter =
                TaskDetailsAdapter().apply {
                    taskDetailsAdapter = this
                }
            adapter = ConcatAdapter(progressAdapter, detailsAdapter)
            layoutManager = LinearLayoutManager(activity)
        }

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(UiR.drawable.ic_arrow_back_white_24dp)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        addMenuProvider()
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressTaskDetails.visible()
                binding.rvTaskDetails.gone()
            } else {
                binding.progressTaskDetails.gone()
                binding.rvTaskDetails.visible()
            }
        }
        viewModel.viewState.taskDetails.observe(viewLifecycleOwner) { items ->
            taskDetailsAdapter?.submitList(items)
        }
        viewModel.viewState.taskMetrics.observe(viewLifecycleOwner) { taskMetrics ->
            taskMetrics?.run { showMetrics(this) } ?: hideMetrics()
        }
        viewModel.viewState.taskProgressDays.observe(viewLifecycleOwner) { renderProgress() }
        viewModel.viewState.taskProgressRange.observe(viewLifecycleOwner) { renderProgress() }
        viewModel.viewState.taskProgressLoading.observe(viewLifecycleOwner) { renderProgress() }
    }

    private fun observeActions() {
        viewModel.viewState.viewAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is TaskDetailsViewAction.ConfirmDeleteTask -> confirmDeleteTask()
                is TaskDetailsViewAction.CloseTaskDetails -> closeTaskDetails(action)
                is TaskDetailsViewAction.NavigateToTaskForm -> navigateToTaskForm(action)
                is TaskDetailsViewAction.ShowErrorLoadingTaskDetails -> showErrorLoadingTaskDetails()
            }
        }
    }

    private fun confirmDeleteTask() {
        deleteTaskDialog =
            TwoOptionsDialog.newInstance(
                message = getString(UiR.string.are_you_sure),
                positiveMsg = UiR.string.delete,
            )
        deleteTaskDialog?.callback = deleteTaskCallback
        deleteTaskDialog?.show(childFragmentManager, DELETE_TASK_TAG)
    }

    private fun closeTaskDetails(action: TaskDetailsViewAction.CloseTaskDetails) {
        val result =
            if (action.success) {
                Activity.RESULT_OK
            } else {
                Activity.RESULT_CANCELED
            }
        activity?.setResult(result)
        activity?.finish()
    }

    private fun navigateToTaskForm(action: TaskDetailsViewAction.NavigateToTaskForm) {
        taskFormNavigator.navigateToEditTask(requireContext(), action.taskId, editTaskLauncher)
    }

    private fun showErrorLoadingTaskDetails() {
        Toast.makeText(activity, UiR.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun addMenuProvider() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater,
                ) {
                    menuInflater.inflate(TaskDetailsR.menu.menu_task_details, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        TaskDetailsR.id.menu_delete -> {
                            return hapticHandled {
                                viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickMenuDelete)
                            }
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
        )
    }

    private fun showMetrics(taskMetrics: TaskMetrics) {
        binding.taskMetrics.tvMetricsDone.text = taskMetrics.doneTasks.toString()
        binding.taskMetrics.tvMetricsNotDone.text = taskMetrics.notDoneTasks.toString()
        binding.taskMetrics.tvMetricsFire.text = taskMetrics.consecutiveDone.toString()

        if (taskMetrics.consecutiveDone == 0) {
            binding.taskMetrics.taskMetricsLlFire.gone()
        } else {
            binding.taskMetrics.taskMetricsLlFire.visible()
        }

        binding.appbar.setExpanded(true, true)
        binding.appbarTaskDetailsFlMetrics.visible()
        binding.taskMetrics.root.visible()
    }

    private fun hideMetrics() {
        binding.taskMetrics.root.gone()
        binding.appbarTaskDetailsFlMetrics.gone()
    }

    private fun renderProgress() {
        val progressDays = viewModel.viewState.taskProgressDays.value.orEmpty()
        val range = viewModel.viewState.taskProgressRange.value ?: TaskProgressRange.LAST_YEAR
        val isLoading = viewModel.viewState.taskProgressLoading.value ?: false

        progressAdapter.submit(progressDays, range, isLoading)
    }

    private fun updateCallbacksFromDialogs() {
        deleteTaskDialog = childFragmentManager.findFragmentByTag(DELETE_TASK_TAG) as? TwoOptionsDialog
        deleteTaskDialog?.callback = deleteTaskCallback
    }
}
