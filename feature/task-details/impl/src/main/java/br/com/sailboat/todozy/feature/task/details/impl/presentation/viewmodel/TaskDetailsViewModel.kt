package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.factory.TaskDetailsUiModelFactory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import kotlinx.coroutines.launch

internal class TaskDetailsViewModel(
    override val viewState: TaskDetailsViewState = TaskDetailsViewState(),
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val disableTaskUseCase: DisableTaskUseCase,
    private val taskDetailsUiModelFactory: TaskDetailsUiModelFactory,
    private val logService: LogService,
) : BaseViewModel<TaskDetailsViewState, TaskDetailsViewAction>() {

    override fun dispatchViewAction(viewAction: TaskDetailsViewAction) {
        when (viewAction) {
            is TaskDetailsViewAction.OnStart -> onStart(viewAction)
            is TaskDetailsViewAction.OnClickMenuDelete -> onClickMenuDelete()
            is TaskDetailsViewAction.OnClickConfirmDeleteTask -> onClickConfirmDeleteTask()
            is TaskDetailsViewAction.OnClickEditTask -> onClickEditTask()
            is TaskDetailsViewAction.OnReturnToDetails -> onReturnToDetails()
        }
    }

    private fun onStart(viewAction: TaskDetailsViewAction.OnStart) {
        viewState.taskId = viewAction.taskId
        loadDetails()
    }

    private fun onClickMenuDelete() {
        viewState.action.value = TaskDetailsViewState.Action.ConfirmDeleteTask
    }

    private fun onClickConfirmDeleteTask() = viewModelScope.launch {
        try {
            val task = getTaskUseCase(viewState.taskId).getOrThrow()
            disableTaskUseCase(task).getOrThrow()
            viewState.action.value = TaskDetailsViewState.Action.CloseTaskDetails(success = true)
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = TaskDetailsViewState.Action.ShowErrorLoadingTaskDetails
        }
    }

    private fun onClickEditTask() {
        viewState.action.value = TaskDetailsViewState.Action.NavigateToTaskForm(viewState.taskId)
    }

    private fun onReturnToDetails() {
        loadDetails()
    }

    private fun loadDetails() = viewModelScope.launch {
        try {
            viewState.loading.postValue(true)

            val task = getTaskUseCase(viewState.taskId).getOrThrow()
            val taskDetails = taskDetailsUiModelFactory.create(task)

            viewState.taskDetails.postValue(taskDetails)

            val taskMetrics: TaskMetrics? = task.alarm?.run {
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
            viewState.action.value = TaskDetailsViewState.Action.ShowErrorLoadingTaskDetails
            viewState.action.value = TaskDetailsViewState.Action.CloseTaskDetails(success = false)
        } finally {
            viewState.loading.postValue(false)
        }
    }
}
