package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator
import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskDetailsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewModel
import br.com.sailboat.uicomponent.impl.helper.NotificationHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import br.com.sailboat.uicomponent.impl.R as UiR

internal class TaskListFragment : Fragment() {
    private val viewModel: TaskListViewModel by viewModel()
    private val taskDetailsNavigator: TaskDetailsNavigator by inject()
    private val taskHistoryNavigator: TaskHistoryNavigator by inject()
    private val taskFormNavigator: TaskFormNavigator by inject()
    private val aboutNavigator: AboutNavigator by inject()
    private val settingsNavigator: SettingsNavigator by inject()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            val tasksLoading by viewModel.viewState.tasksLoading.observeAsState(false)
            val items by viewModel.viewState.itemsView.observeAsState(mutableListOf())
            val taskMetrics by viewModel.viewState.taskMetrics.observeAsState()
            val taskProgressDays by viewModel.viewState.taskProgressDays.observeAsState(emptyList())
            val taskProgressRange by viewModel.viewState.taskProgressRange.observeAsState(
                TaskProgressRange.LAST_YEAR,
            )
            val taskProgressLoading by viewModel.viewState.taskProgressLoading.observeAsState(false)
            val haptics = LocalHapticFeedback.current

            TaskListScreen(
                tasksLoading = tasksLoading,
                items = items,
                taskMetrics = taskMetrics,
                taskProgressDays = taskProgressDays,
                taskProgressRange = taskProgressRange ?: TaskProgressRange.LAST_YEAR,
                taskProgressLoading = taskProgressLoading,
                onSelectProgressRange = { range ->
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.dispatchViewIntent(TaskListViewIntent.OnSelectProgressRange(range))
                },
                onTaskClick = { taskId ->
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.dispatchViewIntent(TaskListViewIntent.OnClickTask(taskId = taskId))
                },
                onTaskSwipe = { taskId, status ->
                    viewModel.dispatchViewIntent(
                        TaskListViewIntent.OnSwipeTask(
                            taskId = taskId,
                            status = status,
                        ),
                    )
                },
                onTaskUndo = { taskId, status ->
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.dispatchViewIntent(
                        TaskListViewIntent.OnClickUndoTask(
                            taskId = taskId,
                            status = status,
                        ),
                    )
                },
                onNewTask = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.dispatchViewIntent(TaskListViewIntent.OnClickNewTask)
                },
                onOpenHistory = { viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuHistory) },
                onOpenSettings = { viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuSettings) },
                onSearch = { term ->
                    viewModel.dispatchViewIntent(
                        TaskListViewIntent.OnSubmitSearchTerm(term = term),
                    )
                },
            )
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
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
                is TaskListViewAction.ShowErrorCompletingTask -> showErrorCompletingTask()
                is TaskListViewAction.ShowErrorLoadingTasks -> showErrorLoadingTasks()
            }
        }
    }

    private fun closeNotifications() {
        activity?.apply { NotificationHelper().closeNotifications(this) }
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

    private fun showErrorLoadingTasks() {
        Toast.makeText(activity, UiR.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorCompletingTask() {
        Toast.makeText(activity, UiR.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAbout() {
        activity?.run { aboutNavigator.navigateToAbout(this) }
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}
