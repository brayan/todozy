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
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.details.impl.R
import br.com.sailboat.todozy.feature.task.details.impl.databinding.FrgTaskDetailsBinding
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewAction
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewIntent
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewIntent.OnClickConfirmDeleteTask
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewModel
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.dialog.twooptions.TwoOptionsDialog
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.putTaskId
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DELETE_TASK_TAG = "DELETE_TASK_TAG"

internal class TaskDetailsFragment : BaseFragment() {

    private val viewModel: TaskDetailsViewModel by viewModel()
    private val taskFormNavigator: TaskFormNavigator by inject()
    private val deleteTaskCallback = object : TwoOptionsDialog.Callback {
        override fun onClickPositiveOption() {
            viewModel.dispatchViewIntent(OnClickConfirmDeleteTask)
        }

        override fun onClickNegativeOption() {}
    }
    private var taskDetailsAdapter: TaskDetailsAdapter? = null
    private var deleteTaskDialog: TwoOptionsDialog? = null

    private val editTaskLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
        savedInstanceState: Bundle?
    ) = FrgTaskDetailsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_details, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        updateCallbacksFromDialogs()

        val taskId = arguments?.getTaskId() ?: Entity.NO_ID
        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickMenuDelete)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun initViews() {
        binding.toolbar.setTitle(R.string.task_details)
        binding.fab.root.setImageResource(R.drawable.ic_edit_white_24dp)
        binding.fab.root.setOnClickListener {
            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickEditTask)
        }

        binding.rvTaskDetails.run {
            adapter = TaskDetailsAdapter().apply {
                taskDetailsAdapter = this
            }
            layoutManager = LinearLayoutManager(activity)
        }

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        binding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
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
        deleteTaskDialog = TwoOptionsDialog.newInstance(
            message = getString(R.string.are_you_sure),
            positiveMsg = R.string.delete
        )
        deleteTaskDialog?.callback = deleteTaskCallback
        deleteTaskDialog?.show(childFragmentManager, DELETE_TASK_TAG)
    }

    private fun closeTaskDetails(action: TaskDetailsViewAction.CloseTaskDetails) {
        val result = if (action.success) {
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
        Toast.makeText(activity, R.string.msg_error, Toast.LENGTH_SHORT).show()
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
    }

    private fun hideMetrics() {
        binding.appbarTaskDetailsFlMetrics.gone()
    }

    private fun updateCallbacksFromDialogs() {
        deleteTaskDialog = childFragmentManager.findFragmentByTag(DELETE_TASK_TAG) as? TwoOptionsDialog
        deleteTaskDialog?.callback = deleteTaskCallback
    }
}
