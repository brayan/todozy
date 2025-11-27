package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.factory.TaskDetailsUiModelFactory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter
import br.com.sailboat.todozy.feature.task.history.domain.usecase.GetTaskProgressUseCase
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.coroutines.DefaultDispatcherProvider
import br.com.sailboat.todozy.utility.kotlin.coroutines.DispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class TaskDetailsViewModel(
    override val viewState: TaskDetailsViewState = TaskDetailsViewState(),
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getTaskProgressUseCase: GetTaskProgressUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val disableTaskUseCase: DisableTaskUseCase,
    private val taskDetailsUiModelFactory: TaskDetailsUiModelFactory,
    private val logService: LogService,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : BaseViewModel<TaskDetailsViewState, TaskDetailsViewIntent>() {
    private var selectedProgressRange = TaskProgressRange.LAST_YEAR
    private var shouldShowProgress = false
    private var progressJob: Job? = null

    override fun dispatchViewIntent(viewIntent: TaskDetailsViewIntent) {
        when (viewIntent) {
            is TaskDetailsViewIntent.OnStart -> onStart(viewIntent)
            is TaskDetailsViewIntent.OnClickMenuDelete -> onClickMenuDelete()
            is TaskDetailsViewIntent.OnClickConfirmDeleteTask -> onClickConfirmDeleteTask()
            is TaskDetailsViewIntent.OnClickEditTask -> onClickEditTask()
            is TaskDetailsViewIntent.OnReturnToDetails -> onReturnToDetails()
            is TaskDetailsViewIntent.OnSelectProgressRange -> onSelectProgressRange(viewIntent.range)
        }
    }

    private fun onStart(viewIntent: TaskDetailsViewIntent.OnStart) {
        viewState.taskId = viewIntent.taskId
        loadDetails()
    }

    private fun onClickMenuDelete() {
        viewState.viewAction.value = TaskDetailsViewAction.ConfirmDeleteTask
    }

    private fun onClickConfirmDeleteTask() =
        viewModelScope.launch {
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

    private fun loadDetails() =
        viewModelScope.launch {
            try {
                viewState.loading.postValue(true)

                val task = getTaskUseCase(viewState.taskId).getOrThrow()
                val taskDetails = taskDetailsUiModelFactory.create(task)

                viewState.taskDetails.postValue(taskDetails)

                shouldShowProgress = false
                val taskMetrics: TaskMetrics? =
                    task.alarm?.run {
                        shouldShowProgress = RepeatType.isAlarmRepeating(this)
                        if (shouldShowProgress) {
                            val filter = TaskHistoryFilter(taskId = viewState.taskId)
                            val taskMetrics = getTaskMetricsUseCase(filter)
                            return@run taskMetrics.getOrNull()
                        } else {
                            shouldShowProgress = false
                            return@run null
                        }
                    }

                viewState.taskMetrics.postValue(taskMetrics)
                loadProgress()
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskDetailsViewAction.ShowErrorLoadingTaskDetails
                viewState.viewAction.value = TaskDetailsViewAction.CloseTaskDetails(success = false)
            } finally {
                viewState.loading.postValue(false)
            }
        }

    private fun onSelectProgressRange(range: TaskProgressRange) {
        if (range == selectedProgressRange) {
            return
        }
        selectedProgressRange = range
        viewState.taskProgressRange.postValue(range)

        loadProgress(force = true)
    }

    private fun loadProgress(force: Boolean = false) {
        val shouldFetch = force || shouldShowProgress
        if (shouldFetch.not()) {
            viewState.taskProgressDays.postValue(emptyList())
            viewState.taskProgressLoading.postValue(false)
            return
        }

        progressJob?.cancel()
        viewState.taskProgressDays.postValue(emptyList())
        progressJob =
            viewModelScope.launch {
                try {
                    viewState.taskProgressLoading.postValue(true)

                    val progress =
                        withContext(dispatcherProvider.default()) {
                            getTaskProgressUseCase(
                                TaskProgressFilter(
                                    range = selectedProgressRange,
                                    taskId = viewState.taskId,
                                ),
                            ).getOrThrow()
                        }
                    viewState.taskProgressRange.postValue(selectedProgressRange)
                    viewState.taskProgressDays.postValue(progress)
                } catch (throwable: Exception) {
                    logService.error(throwable)
                    viewState.taskProgressDays.postValue(emptyList())
                } finally {
                    viewState.taskProgressLoading.postValue(false)
                }
            }
    }
}
