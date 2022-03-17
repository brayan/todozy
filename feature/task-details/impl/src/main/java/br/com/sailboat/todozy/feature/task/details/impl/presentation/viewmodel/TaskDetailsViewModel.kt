package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewAction.*
import br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel.TaskDetailsViewState.Action.*
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TaskDetailsViewModel(
    override val viewState: TaskDetailsViewState = TaskDetailsViewState(),
    private val getTaskDetailsViewUseCase: GetTaskDetailsViewUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val disableTaskUseCase: DisableTaskUseCase,
    private val logService: LogService,
) : BaseViewModel<TaskDetailsViewState, TaskDetailsViewAction>() {

    override fun dispatchViewAction(viewAction: TaskDetailsViewAction) {
        when (viewAction) {
            is OnStart -> onStart(viewAction)
            is OnClickMenuDelete -> onClickMenuDelete()
            is OnClickConfirmDeleteTask -> onClickConfirmDeleteTask()
            is OnClickEditTask -> onClickEditTask()
            is OnReturnToDetails -> onReturnToDetails()
        }
    }

    private fun onStart(viewAction: OnStart) {
        viewState.taskId = viewAction.taskId
        loadDetails()
    }

    private fun onClickMenuDelete() {
        viewState.action.value = ConfirmDeleteTask
    }

    private fun onClickConfirmDeleteTask() = viewModelScope.launch {
        try {
            val task = getTaskUseCase(viewState.taskId).getOrThrow()
            disableTaskUseCase(task).getOrThrow()
            viewState.action.value = CloseTaskDetails(success = true)
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = ShowErrorLoadingTaskDetails
        }
    }

    private fun onClickEditTask() {
        viewState.action.value = NavigateToTaskForm(viewState.taskId)
    }

    private fun onReturnToDetails() {
        loadDetails()
    }

    private fun loadDetails() = viewModelScope.launch {
        try {
            val taskDetails = async { getTaskDetailsViewUseCase(viewState.taskId) }
            val alarm = async { getAlarmUseCase(viewState.taskId) }

            viewState.taskDetails.postValue(taskDetails.await().getOrThrow())

            val taskMetrics: TaskMetrics? = alarm.await().getOrNull()?.run {
                if (RepeatType.isAlarmRepeating(this)) {
                    val filter = TaskHistoryFilter(taskId = viewState.taskId)
                    val taskMetrics = getTaskMetricsUseCase(filter)
                    return@run taskMetrics.getOrNull()
                } else {
                    return@run null
                }
            }

            viewState.taskMetrics.postValue(taskMetrics)
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = ShowErrorLoadingTaskDetails
            viewState.action.value = CloseTaskDetails(success = false)
        }
    }

}