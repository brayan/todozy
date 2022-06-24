package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.impl.presentation.factory.TaskListUiModelFactory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnClickMenuAbout
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnClickMenuHistory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnClickMenuSettings
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnClickNewTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnClickTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnStart
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnSubmitSearchTerm
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.OnSwipeTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.CloseNotifications
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.NavigateToAbout
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.NavigateToHistory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.NavigateToSettings
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.NavigateToTaskDetails
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.NavigateToTaskForm
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.ShowErrorCompletingTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.ShowErrorLoadingTasks
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.UpdateRemovedTask
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.uicomponent.model.TaskUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

private const val TASK_SWIPE_DELAY_IN_MILLIS = 4000L

internal class TaskListViewModel(
    override val viewState: TaskListViewState = TaskListViewState(),
    private val getTasksUseCase: GetTasksUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val taskListUiModelFactory: TaskListUiModelFactory,
    private val logService: LogService,
) : BaseViewModel<TaskListViewState, TaskListViewAction>() {

    private var filter = TaskFilter(TaskCategory.TODAY)
    private val swipeTaskAsyncJobs: MutableList<Job> = mutableListOf()

    override fun dispatchViewAction(viewAction: TaskListViewAction) {
        when (viewAction) {
            is OnStart -> onStart()
            is OnClickMenuAbout -> onClickMenuAbout()
            is OnClickMenuSettings -> onClickMenuSettings()
            is OnClickMenuHistory -> onClickMenuHistory()
            is OnClickNewTask -> onClickNewTask()
            is OnClickTask -> onClickTask(viewAction.taskId)
            is OnSubmitSearchTerm -> onSubmitSearchTerm(viewAction.term)
            is OnSwipeTask -> onSwipeTask(viewAction.position, viewAction.status)
        }
    }

    private fun onStart() = viewModelScope.launch {
        try {
            viewState.action.postValue(CloseNotifications)
            loadTasks()
            scheduleAllAlarmsUseCase()
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = ShowErrorLoadingTasks
        }
    }

    private fun onClickMenuAbout() {
        viewState.action.value = NavigateToAbout
    }

    private fun onClickMenuSettings() {
        viewState.action.value = NavigateToSettings
    }

    private fun onClickMenuHistory() {
        viewState.action.value = NavigateToHistory
    }

    private fun onClickNewTask() {
        viewState.action.value = NavigateToTaskForm
    }

    private fun onClickTask(taskId: Long) {
        viewState.action.value = NavigateToTaskDetails(taskId = taskId)
    }

    private fun onSubmitSearchTerm(term: String) = viewModelScope.launch {
        try {
            filter.text = term
            loadTasks()
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = ShowErrorLoadingTasks
        }
    }

    private fun loadTasks() = viewModelScope.launch {
        viewState.loading.postValue(true)

        val taskCategories = listOf(
            TaskCategory.BEFORE_TODAY,
            TaskCategory.TODAY,
            TaskCategory.TOMORROW,
            TaskCategory.NEXT_DAYS,
        )

        val tasks = taskCategories.map { category ->
            async {
                val filter = TaskFilter(category).apply { text = filter.text }
                val tasks = getTasksUseCase(filter).getOrThrow()

                taskListUiModelFactory.create(tasks, category)
            }
        }.awaitAll().flatten()

        viewState.itemsView.postValue(tasks.toMutableList())
        viewState.loading.postValue(false)
    }

    private fun onSwipeTask(position: Int, status: TaskStatus) = launchSwipeTask {
        try {
            viewState.taskMetrics.value = null

            val itemsView = viewState.itemsView.value

            val taskId = (itemsView?.get(position) as TaskUiModel).taskId

            completeTaskUseCase(taskId, status)

            itemsView.removeAt(position)
            viewState.action.postValue(UpdateRemovedTask(position))

            viewState.itemsView.postValue(itemsView)

            val alarm = getAlarmUseCase(taskId).getOrNull()

            alarm?.run {
                if (RepeatType.isAlarmRepeating(alarm)) {
                    val filter = TaskHistoryFilter(taskId = taskId)
                    viewState.taskMetrics.value = getTaskMetricsUseCase(filter).getOrNull()
                }
            }

            delay(TASK_SWIPE_DELAY_IN_MILLIS)

            if (swipeTaskAsyncJobs.size == 1) {
                loadTasks()
                viewState.taskMetrics.value = null
            }
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = ShowErrorCompletingTask
        }
    }

    private fun launchSwipeTask(block: suspend CoroutineScope.() -> Unit) {
        val job: Job = viewModelScope.launch {
            supervisorScope { block() }
        }
        swipeTaskAsyncJobs.add(job)
        job.invokeOnCompletion { swipeTaskAsyncJobs.remove(job) }
    }
}
