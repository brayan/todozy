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
) : BaseViewModel<TaskDetailsViewState, TaskDetailsViewIntent>() {

    override fun dispatchViewIntent(viewIntent: TaskDetailsViewIntent) {
        when (viewIntent) {
            is TaskDetailsViewIntent.OnStart -> onStart(viewIntent)
            is TaskDetailsViewIntent.OnClickMenuDelete -> onClickMenuDelete()
            is TaskDetailsViewIntent.OnClickConfirmDeleteTask -> onClickConfirmDeleteTask()
            is TaskDetailsViewIntent.OnClickEditTask -> onClickEditTask()
            is TaskDetailsViewIntent.OnReturnToDetails -> onReturnToDetails()
        }
    }

    private fun onStart(viewIntent: TaskDetailsViewIntent.OnStart) {
        viewState.taskId = viewIntent.taskId
        loadDetails()
    }

    private fun onClickMenuDelete() {
        viewState.viewAction.value = TaskDetailsViewAction.ConfirmDeleteTask
    }

    private fun onClickConfirmDeleteTask() = viewModelScope.launch {
        try {
            val task = getTaskUseCase(viewState.taskId).getOrThrow()
            disableTaskUseCase(task).getOrThrow()
            viewState.viewAction.value = TaskDetailsViewAction.CloseTaskDetails(success = true)
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskDetailsViewAction.ShowErrorLoadingTaskDetails
        }
    }

    private fun onClickEditTask() {
        viewState.viewAction.value = TaskDetailsViewAction.NavigateToTaskForm(viewState.taskId)
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
            viewState.viewAction.value = TaskDetailsViewAction.ShowErrorLoadingTaskDetails
            viewState.viewAction.value = TaskDetailsViewAction.CloseTaskDetails(success = false)
        } finally {
            viewState.loading.postValue(false)
        }
    }
}
