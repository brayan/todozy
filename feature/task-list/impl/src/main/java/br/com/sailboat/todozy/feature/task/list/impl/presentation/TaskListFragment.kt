package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator
import br.com.sailboat.todozy.feature.navigation.android.HomeDestination
import br.com.sailboat.todozy.feature.navigation.android.HomeTabNavigator
import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskDetailsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewModel
import br.com.sailboat.uicomponent.impl.helper.NotificationHelper
import kotlinx.coroutines.launch
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
    private var forceReloadOnResume = false

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                forceReloadOnResume = true
            }
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
            val tasksLoading by viewModel.viewState.tasksLoading.collectAsStateWithLifecycle(false)
            val items by viewModel.viewState.itemsView.collectAsStateWithLifecycle(emptyList())
            val taskMetrics by viewModel.viewState.taskMetrics.collectAsStateWithLifecycle()
            val taskProgressDays by viewModel.viewState.taskProgressDays.collectAsStateWithLifecycle(emptyList())
            val taskProgressRange by viewModel.viewState.taskProgressRange.collectAsStateWithLifecycle(
                TaskProgressRange.LAST_YEAR,
            )
            val taskProgressLoading by viewModel.viewState.taskProgressLoading.collectAsStateWithLifecycle(false)
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
                onMarkDoneForToday = { taskId ->
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.dispatchViewIntent(TaskListViewIntent.OnMarkDoneForToday(taskId))
                },
                onNewTask = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.dispatchViewIntent(TaskListViewIntent.OnClickNewTask)
                },
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
        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatchViewIntent(
            TaskListViewIntent.OnResume(forceReload = forceReloadOnResume),
        )
        forceReloadOnResume = false
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.viewAction.collect { viewAction ->
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
                    viewModel.viewState.viewAction.resetReplayCache()
                }
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
        val homeNavigator = activity as? HomeTabNavigator
        if (homeNavigator != null) {
            homeNavigator.switchTo(HomeDestination.SETTINGS)
        } else {
            settingsNavigator.navigateToSettings(requireContext(), launcher)
        }
    }

    private fun navigateToTaskDetails(taskId: Long) {
        taskDetailsNavigator.navigateToTaskDetails(requireContext(), taskId, launcher)
    }

    private fun navigateToHistory() {
        val homeNavigator = activity as? HomeTabNavigator
        if (homeNavigator != null) {
            homeNavigator.switchTo(HomeDestination.HISTORY)
        } else {
            taskHistoryNavigator.navigateToTaskHistory(requireContext())
        }
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
}
