package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter
import br.com.sailboat.todozy.feature.task.history.domain.usecase.GetTaskProgressUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.impl.presentation.factory.TaskListUiModelFactory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnClickMenuAbout
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnClickMenuHistory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnClickMenuSettings
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnClickNewTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnClickTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnSelectProgressRange
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnStart
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnSubmitSearchTerm
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnSwipeTask
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.coroutines.DefaultDispatcherProvider
import br.com.sailboat.todozy.utility.kotlin.coroutines.DispatcherProvider
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.model.TaskUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.time.LocalDate

private const val TASK_SWIPE_DELAY_IN_MILLIS = 4000L

internal class TaskListViewModel(
    override val viewState: TaskListViewState = TaskListViewState(),
    private val getTasksUseCase: GetTasksUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getTaskProgressUseCase: GetTaskProgressUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val taskListUiModelFactory: TaskListUiModelFactory,
    private val logService: LogService,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : BaseViewModel<TaskListViewState, TaskListViewIntent>() {
    private var taskFilter = TaskFilter(category = TaskCategory.TODAY)
    private var selectedProgressRange = TaskProgressRange.LAST_YEAR
    private val swipeTaskAsyncJobs: MutableList<Job> = mutableListOf()
    private var progressJob: Job? = null
    private val progressCache: MutableMap<ProgressCacheKey, List<TaskProgressDay>> = mutableMapOf()

    override fun dispatchViewIntent(viewIntent: TaskListViewIntent) {
        when (viewIntent) {
            is OnStart -> onStart()
            is OnClickMenuAbout -> onClickMenuAbout()
            is OnClickMenuSettings -> onClickMenuSettings()
            is OnClickMenuHistory -> onClickMenuHistory()
            is OnClickNewTask -> onClickNewTask()
            is OnClickTask -> onClickTask(viewIntent.taskId)
            is OnSubmitSearchTerm -> onSubmitSearchTerm(viewIntent.term)
            is OnSwipeTask -> onSwipeTask(viewIntent.position, viewIntent.status)
            is OnSelectProgressRange -> onSelectProgressRange(viewIntent.range)
        }
    }

    private fun onStart() =
        viewModelScope.launch {
            try {
                viewState.loading.postValue(true)
                viewState.viewAction.postValue(TaskListViewAction.CloseNotifications)
                loadTasks()
                loadProgress()
                scheduleAllAlarmsUseCase()
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskListViewAction.ShowErrorLoadingTasks
            } finally {
                viewState.loading.postValue(false)
            }
        }

    private fun onClickMenuAbout() {
        viewState.viewAction.value = TaskListViewAction.NavigateToAbout
    }

    private fun onClickMenuSettings() {
        viewState.viewAction.value = TaskListViewAction.NavigateToSettings
    }

    private fun onClickMenuHistory() {
        viewState.viewAction.value = TaskListViewAction.NavigateToHistory
    }

    private fun onClickNewTask() {
        viewState.viewAction.value = TaskListViewAction.NavigateToTaskForm
    }

    private fun onClickTask(taskId: Long) {
        viewState.viewAction.value = TaskListViewAction.NavigateToTaskDetails(taskId = taskId)
    }

    private fun onSubmitSearchTerm(term: String) =
        viewModelScope.launch {
            try {
                viewState.loading.postValue(true)
                val hasQueryChanged = taskFilter.text != term
                taskFilter = taskFilter.copy(text = term)
                if (hasQueryChanged) {
                    clearProgressCache()
                }
                loadTasks()
                loadProgress(force = true)
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskListViewAction.ShowErrorLoadingTasks
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
        loadProgress()
    }

    private fun loadProgress(force: Boolean = false) {
        val cacheKey = currentProgressCacheKey()
        val cachedProgress = progressCache[cacheKey]
        if (cachedProgress != null && force.not()) {
            viewState.taskProgressRange.postValue(selectedProgressRange)
            viewState.taskProgressDays.postValue(cachedProgress)
            viewState.taskProgressLoading.postValue(false)
            return
        }

        progressJob?.cancel()
        progressJob =
            viewModelScope.launch {
                try {
                    viewState.taskProgressLoading.postValue(true)

                    val progressFilter =
                        TaskProgressFilter(
                            range = selectedProgressRange,
                            taskId = Entity.NO_ID,
                            text = taskFilter.text,
                        )
                    val progress =
                        withContext(dispatcherProvider.default()) {
                            getTaskProgressUseCase(progressFilter).getOrThrow()
                        }
                    progressCache[cacheKey] = progress
                    viewState.taskProgressRange.postValue(selectedProgressRange)
                    viewState.taskProgressDays.postValue(progress)
                } catch (e: Exception) {
                    logService.error(e)
                } finally {
                    viewState.taskProgressLoading.postValue(false)
                }
            }
    }

    private suspend fun loadTasks() =
        coroutineScope {
            val taskCategories =
                listOf(
                    TaskCategory.BEFORE_TODAY,
                    TaskCategory.TODAY,
                    TaskCategory.TOMORROW,
                    TaskCategory.NEXT_DAYS,
                )

            val tasks =
                taskCategories.map { category ->
                    async {
                        val filter =
                            TaskFilter(
                                text = taskFilter.text,
                                category = category,
                            )
                        val tasks = getTasksUseCase(filter).getOrThrow()

                        taskListUiModelFactory.create(tasks, category)
                    }
                }.awaitAll().flatten()

            viewState.itemsView.postValue(tasks.toMutableList())
        }

    private fun onSwipeTask(
        position: Int,
        status: TaskStatus,
    ) = launchSwipeTask {
        try {
            viewState.taskMetrics.value = null

            val itemsView = viewState.itemsView.value

            val taskId = (itemsView?.get(position) as TaskUiModel).taskId

            completeTaskUseCase(taskId, status)

            itemsView.removeAt(position)
            viewState.viewAction.postValue(TaskListViewAction.UpdateRemovedTask(position))

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
                updateTodayProgress(status)
            }
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskListViewAction.ShowErrorCompletingTask
        }
    }

    private fun launchSwipeTask(block: suspend CoroutineScope.() -> Unit) {
        val job: Job =
            viewModelScope.launch {
                supervisorScope { block() }
            }
        swipeTaskAsyncJobs.add(job)
        job.invokeOnCompletion { swipeTaskAsyncJobs.remove(job) }
    }

    private fun updateTodayProgress(status: TaskStatus) {
        val today = LocalDate.now()
        val cacheKey = currentProgressCacheKey()
        progressCache.putIfAbsent(cacheKey, viewState.taskProgressDays.value.orEmpty())
        val updatedCache =
            progressCache.mapValues { (key, days) ->
                if (key.searchTerm == taskFilter.text.orEmpty()) {
                    updateProgressDays(days, today, status)
                } else {
                    days
                }
            }.toMutableMap()

        progressCache.clear()
        progressCache.putAll(updatedCache)

        progressCache[cacheKey]?.let { updatedDays ->
            viewState.taskProgressDays.postValue(updatedDays)
            viewState.taskProgressRange.postValue(selectedProgressRange)
        }
    }

    private fun clearProgressCache() {
        progressCache.clear()
    }

    private fun currentProgressCacheKey() =
        ProgressCacheKey(
            range = selectedProgressRange,
            searchTerm = taskFilter.text.orEmpty(),
        )

    private fun updateProgressDays(
        days: List<TaskProgressDay>,
        today: LocalDate,
        status: TaskStatus,
    ): List<TaskProgressDay> {
        if (days.isEmpty()) {
            val doneCount = if (status == TaskStatus.DONE) 1 else 0
            return listOf(
                TaskProgressDay(
                    date = today,
                    doneCount = doneCount,
                    totalCount = 1,
                ),
            )
        }

        val existingIndex = days.indexOfFirst { it.date == today }
        val doneDelta = if (status == TaskStatus.DONE) 1 else 0

        val updatedDay =
            if (existingIndex >= 0) {
                val current = days[existingIndex]
                current.copy(
                    doneCount = current.doneCount + doneDelta,
                    totalCount = current.totalCount + 1,
                )
            } else {
                TaskProgressDay(
                    date = today,
                    doneCount = doneDelta,
                    totalCount = 1,
                )
            }

        val updatedDays = days.toMutableList()
        if (existingIndex >= 0) {
            updatedDays[existingIndex] = updatedDay
        } else {
            updatedDays.add(updatedDay)
        }

        return updatedDays.sortedBy { it.date }
    }
}

private data class ProgressCacheKey(
    val range: TaskProgressRange,
    val searchTerm: String,
)
