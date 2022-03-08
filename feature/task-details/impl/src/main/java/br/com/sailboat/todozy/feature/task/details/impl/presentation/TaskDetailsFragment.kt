package br.com.sailboat.todozy.feature.task.details.impl.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.feature.task.details.impl.R
import br.com.sailboat.todozy.feature.task.details.impl.databinding.FrgTaskDetailsBinding
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewModel
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewAction
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewState.Action.*
import br.com.sailboat.todozy.feature.task.form.presentation.navigator.TaskFormNavigator
import br.com.sailboat.todozy.uicomponent.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.uicomponent.helper.DialogHelper
import br.com.sailboat.todozy.uicomponent.helper.getTaskId
import br.com.sailboat.todozy.uicomponent.helper.putTaskId
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskDetailsFragment : BaseFragment() {

    private val viewModel: TaskDetailsViewModel by viewModel()
    private val taskFormNavigator: TaskFormNavigator by inject()

    private var taskDetailsAdapter: TaskDetailsAdapter? = null

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

        val taskId = arguments?.getTaskId() ?: Entity.NO_ID
        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)
    }

    override fun initViews() {
        binding.toolbar.setTitle(R.string.task_details)
        binding.fab.root.setImageResource(R.drawable.ic_edit_white_24dp)
        binding.fab.root.setOnClickListener {
            viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickEditTask)
        }

        binding.recycler.recycler.run {
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
        viewModel.viewState.taskDetails.observe(viewLifecycleOwner) { items ->
            taskDetailsAdapter?.submitList(items)
        }
        viewModel.viewState.taskMetrics.observe(viewLifecycleOwner) { taskMetrics ->
            taskMetrics?.run { showMetrics(this) } ?: hideMetrics()
        }
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is ConfirmDeleteTask -> confirmDeleteTask()
                is CloseTaskDetails -> closeTaskDetails(action)
                is NavigateToTaskForm -> navigateToTaskForm(action)
                is ShowErrorLoadingTaskDetails -> showErrorLoadingTaskDetails()
            }
        }
    }

    private fun confirmDeleteTask() {
        activity?.run {
            DialogHelper().showDeleteDialog(
                childFragmentManager,
                this,
                object : TwoOptionsDialog.PositiveCallback {
                    override fun onClickPositiveOption() {
                        viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickConfirmDeleteTask)
                    }
                })
        }
    }

    private fun closeTaskDetails(action: CloseTaskDetails) {
        val result = if (action.success) {
            Activity.RESULT_OK
        } else {
            Activity.RESULT_CANCELED
        }
        activity?.setResult(result)
        activity?.finish()
    }

    private fun navigateToTaskForm(action: NavigateToTaskForm) {
        taskFormNavigator.navigateToEditTaskForm(this, action.taskId)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickMenuDelete)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}