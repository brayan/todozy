package br.com.sailboat.todozy.feature.task.details.impl.presentation

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.TooltipCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.details.impl.databinding.FrgTaskDetailsBinding
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewAction
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewIntent
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewIntent.OnClickConfirmDeleteTask
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewModel
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.setSafeClickListener
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.dialog.twooptions.TwoOptionsDialog
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.putTaskId
import br.com.sailboat.uicomponent.impl.progress.TaskProgressHeaderAdapter
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
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
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK)
            }
            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnReturnToDetails)
        }

    companion object {
        fun newInstance(taskId: Long): TaskDetailsFragment = with(TaskDetailsFragment()) {
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
                    highlightNotDone = true,
                    flatColors = true,
                )
            val detailsAdapter =
                TaskDetailsAdapter().apply {
                    taskDetailsAdapter = this
                }
            adapter = ConcatAdapter(progressAdapter, detailsAdapter)
            layoutManager = LinearLayoutManager(activity)
        }

        binding.taskDetailsTopBar.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        binding.taskDetailsTopBar.setContent {
            TodozyTheme {
                val taskMetrics by viewModel.viewState.taskMetrics.observeAsState()
                TaskDetailsTopBar(
                    taskMetrics = taskMetrics,
                    onNavigateBack = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                    onClickDelete = { viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickMenuDelete) },
                )
            }
        }
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
        viewModel.viewState.taskProgressDays.observe(viewLifecycleOwner) { renderProgress() }
        viewModel.viewState.taskProgressDayOrder.observe(viewLifecycleOwner) { renderProgress() }
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

    private fun renderProgress() {
        val progressDays = viewModel.viewState.taskProgressDays.value.orEmpty()
        val range = viewModel.viewState.taskProgressRange.value ?: TaskProgressRange.LAST_YEAR
        val isLoading = viewModel.viewState.taskProgressLoading.value ?: false
        val daysOfWeek = viewModel.viewState.taskProgressDayOrder.value.orEmpty()

        progressAdapter.submit(progressDays, range, isLoading, daysOfWeek)
    }

    private fun updateCallbacksFromDialogs() {
        deleteTaskDialog = childFragmentManager.findFragmentByTag(DELETE_TASK_TAG) as? TwoOptionsDialog
        deleteTaskDialog?.callback = deleteTaskCallback
    }
}
