package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.model.Entity
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

    private var taskId: Long = Entity.NO_ID

    override fun dispatchViewAction(viewAction: TaskDetailsViewAction) {
        when (viewAction) {
            is TaskDetailsViewAction.OnStart -> onStart(viewAction)
            is TaskDetailsViewAction.OnClickMenuDelete -> onClickMenuDelete()
            is TaskDetailsViewAction.OnClickConfirmDeleteTask -> onClickConfirmDeleteTask()
            is TaskDetailsViewAction.OnClickEditTask -> onClickEditTask()
        }
    }

    private fun onStart(viewAction: TaskDetailsViewAction.OnStart) {
        this.taskId = viewAction.taskId
        loadDetails()
    }

    private fun onClickMenuDelete() {
        viewState.action.value = TaskDetailsViewState.Action.ConfirmDeleteTask
    }

    private fun onClickConfirmDeleteTask() = viewModelScope.launch {
        try {
            val task = getTaskUseCase(taskId)
            disableTaskUseCase(task)
        } catch (e: Exception) {
            logService.error(e)
        }
    }

    private fun onClickEditTask() {
        viewState.action.value = TaskDetailsViewState.Action.NavigateToTaskForm(taskId)
    }

    private fun loadDetails() = viewModelScope.launch {
        try {
            val taskDetails = getTaskDetailsViewUseCase(taskId)
            viewState.taskDetails.postValue(taskDetails)

            val taskMetrics = getTaskMetricsUseCase(TaskHistoryFilter(taskId = taskId))
            viewState.taskMetrics.postValue(taskMetrics)

            val alarm = getAlarmUseCase(taskId)

            alarm?.run {
                if (RepeatType.isAlarmRepeating(alarm)) {
                    viewState.taskMetrics.value =
                        getTaskMetricsUseCase(TaskHistoryFilter(taskId = taskId))
                }
            }

        } catch (e: Exception) {
            logService.error(e)
        }
    }

}