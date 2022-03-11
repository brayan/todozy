package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.*
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.impl.presentation.GetTasksViewUseCase
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewAction.*
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewState.Action.*
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import kotlinx.coroutines.*

class TaskListViewModel(
    override val viewState: TaskListViewState = TaskListViewState(),
    private val getTasksViewUseCase: GetTasksViewUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
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

    private suspend fun loadTasks() {
        viewState.loading.postValue(true)
        val tasks = getTasksViewUseCase(filter.text).getOrThrow()
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
                    viewState.taskMetrics.value =
                        getTaskMetricsUseCase(TaskHistoryFilter(taskId = taskId))
                }
            }

            delay(4000)

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