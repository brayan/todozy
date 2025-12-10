package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
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
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnClickUndoTask
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnSelectProgressRange
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnStart
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnSubmitSearchTerm
import br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel.TaskListViewIntent.OnSwipeTask
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.coroutines.DefaultDispatcherProvider
import br.com.sailboat.todozy.utility.kotlin.coroutines.DispatcherProvider
import br.com.sailboat.todozy.utility.kotlin.extension.toEndOfDayCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toStartOfDayCalendar
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.model.UiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

private const val TASK_SWIPE_DELAY_IN_MILLIS = 4000L
private const val SEARCH_DEBOUNCE_IN_MILLIS = 300L

internal class TaskListViewModel(
    override val viewState: TaskListViewState = TaskListViewState(),
    private val getTasksUseCase: GetTasksUseCase,
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getTaskProgressUseCase: GetTaskProgressUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val taskListUiModelFactory: TaskListUiModelFactory,
    private val logService: LogService,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : BaseViewModel<TaskListViewState, TaskListViewIntent>() {
    private val taskCategories =
        listOf(
            TaskCategory.BEFORE_TODAY,
            TaskCategory.TODAY,
            TaskCategory.TOMORROW,
            TaskCategory.NEXT_DAYS,
        )
    private var taskFilter = TaskFilter(category = TaskCategory.TODAY)
    private var selectedProgressRange = TaskProgressRange.LAST_YEAR
    private var currentTasks: List<Task> = emptyList()
    private var currentTasksByCategory: Map<TaskCategory, List<Task>> = emptyMap()
    private var baseTaskMetrics: TaskMetrics? = null
    private val baseTaskConsecutive: MutableMap<Long, Int> = mutableMapOf()
    private val inlineTaskFeedbacks: MutableMap<Long, InlineTaskFeedback> = mutableMapOf()
    private val inlineTaskJobs: MutableMap<Long, Job> = mutableMapOf()
    private var progressJob: Job? = null
    private val progressCache: MutableMap<ProgressCacheKey, List<TaskProgressDay>> = mutableMapOf()
    private var searchJob: Job? = null
    private var hasLoaded = false
    private var lastFullLoadDate: LocalDate? = null

    override fun dispatchViewIntent(viewIntent: TaskListViewIntent) {
        when (viewIntent) {
            is OnStart -> onStart()
            is TaskListViewIntent.OnResume -> onResume(viewIntent.forceReload)
            is OnClickMenuAbout -> onClickMenuAbout()
            is OnClickMenuSettings -> onClickMenuSettings()
            is OnClickMenuHistory -> onClickMenuHistory()
            is OnClickNewTask -> onClickNewTask()
            is OnClickTask -> onClickTask(viewIntent.taskId)
            is OnSubmitSearchTerm -> onSubmitSearchTerm(viewIntent.term)
            is OnClickUndoTask -> onClickUndoTask(viewIntent.taskId)
            is OnSwipeTask -> onSwipeTask(viewIntent.taskId, viewIntent.status)
            is OnSelectProgressRange -> onSelectProgressRange(viewIntent.range)
        }
    }

    private fun onStart() = viewModelScope.launch {
        performFullLoad(closeNotifications = true)
    }

    private fun onResume(forceReload: Boolean) = viewModelScope.launch {
        if (hasLoaded.not()) {
            return@launch
        }

        val today = LocalDate.now()
        val midnightPassed = lastFullLoadDate?.isBefore(today) == true

        when {
            forceReload -> performFullLoad(closeNotifications = true)
            midnightPassed -> performFullLoad(closeNotifications = true)
            else -> refreshTasksFromCache()
        }
    }

    private suspend fun performFullLoad(closeNotifications: Boolean = false) {
        try {
            viewState.tasksLoading.postValue(true)
            viewState.taskProgressLoading.postValue(true)
            if (closeNotifications) {
                viewState.viewAction.postValue(TaskListViewAction.CloseNotifications)
            }
            loadTasks()
            viewState.tasksLoading.postValue(false)
            loadTaskMetrics()
            loadProgress()
            scheduleAllAlarmsUseCase()
            lastFullLoadDate = LocalDate.now()
            hasLoaded = true
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskListViewAction.ShowErrorLoadingTasks
        } finally {
            viewState.taskProgressLoading.postValue(false)
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

    private fun onSubmitSearchTerm(term: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                delay(SEARCH_DEBOUNCE_IN_MILLIS)
                viewState.tasksLoading.postValue(true)
                viewState.taskProgressLoading.postValue(true)
                val hasQueryChanged = taskFilter.text != term
                taskFilter = taskFilter.copy(text = term)
                if (hasQueryChanged) {
                    clearProgressCache()
                }
                loadTasks()
                loadTaskMetrics()
                loadProgress(force = true)
            } catch (_: CancellationException) {
                return@launch
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskListViewAction.ShowErrorLoadingTasks
            } finally {
                viewState.tasksLoading.postValue(false)
                viewState.taskProgressLoading.postValue(false)
            }
        }
    }

    private fun onSelectProgressRange(range: TaskProgressRange) {
        if (range == selectedProgressRange) {
            return
        }
        selectedProgressRange = range
        viewState.taskProgressRange.postValue(range)
        loadProgress()
        viewModelScope.launch { loadTaskMetrics() }
    }

    private fun loadProgress(force: Boolean = false) {
        if (currentTasks.isEmpty()) {
            progressJob?.cancel()
            progressCache.remove(currentProgressCacheKey())
            viewState.taskProgressLoading.postValue(false)
            viewState.taskProgressRange.postValue(selectedProgressRange)
            viewState.taskProgressDays.postValue(emptyList())
            return
        }

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
                    val progress = withContext(dispatcherProvider.default()) {
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

    private suspend fun loadTasks() = coroutineScope {
        val domainTasksByCategory = mutableMapOf<TaskCategory, List<Task>>()
        val tasksUiModels = taskCategories.map { category ->
            async {
                val filter = TaskFilter(
                    text = taskFilter.text,
                    category = category,
                )
                val tasks = withContext(dispatcherProvider.io()) {
                    getTasksUseCase(filter).getOrThrow()
                }

                domainTasksByCategory[category] = tasks
                taskListUiModelFactory.create(tasks, category)
            }
        }.awaitAll().flatten()

        val itemsWithFeedback = applyInlineFeedbacks(tasksUiModels)
        currentTasksByCategory = domainTasksByCategory.toMap()
        currentTasks = taskCategories.flatMap { category ->
            domainTasksByCategory[category].orEmpty()
        }
        viewState.itemsView.postValue(itemsWithFeedback.toMutableList())
    }

    private fun refreshTasksFromCache() {
        if (currentTasksByCategory.isEmpty()) {
            return
        }

        val uiModels = taskCategories.map { category ->
            val tasks = currentTasksByCategory[category].orEmpty()
            taskListUiModelFactory.create(tasks, category)
        }.flatten()

        val itemsWithFeedback = applyInlineFeedbacks(uiModels)
        viewState.itemsView.postValue(itemsWithFeedback.toMutableList())
    }

    private suspend fun loadTaskMetrics() {
        runCatching {
            coroutineScope {
                val taskIds = if (currentTasks.isNotEmpty()) {
                    currentTasks.map { it.id }
                } else {
                    viewState.itemsView.value
                        ?.mapNotNull { (it as? TaskUiModel)?.taskId }
                        .orEmpty()
                }

                if (taskIds.isEmpty()) {
                    baseTaskConsecutive.clear()
                    return@coroutineScope null
                }

                val globalMetricsDeferred = async(dispatcherProvider.default()) {
                    val filter = historyFilterForRange()
                    getTaskMetricsUseCase(filter).getOrThrow()
                }

                val perTaskConsecutiveDeferred = async(dispatcherProvider.default()) {
                    if (taskIds.isEmpty()) {
                        emptyMap()
                    } else {
                        val deferredMap = taskIds.associateWith { taskId ->
                            async {
                                val filter = historyFilterForRange(taskId, includeText = true)
                                getTaskMetricsUseCase(filter).getOrThrow().consecutiveDone
                            }
                        }
                        deferredMap.mapValues { it.value.await() }
                    }
                }

                val globalMetrics = globalMetricsDeferred.await()
                val perTaskConsecutive = perTaskConsecutiveDeferred.await()
                val totalConsecutive = perTaskConsecutive.values.sum()

                baseTaskConsecutive.clear()
                baseTaskConsecutive.putAll(perTaskConsecutive)

                TaskMetrics(
                    doneTasks = globalMetrics.doneTasks,
                    notDoneTasks = globalMetrics.notDoneTasks,
                    consecutiveDone = totalConsecutive,
                )
            }
        }.onSuccess { metrics ->
            if (metrics == null) {
                baseTaskMetrics = null
                viewState.taskMetrics.postValue(null)
            } else {
                baseTaskMetrics = metrics
                publishTaskMetricsWithPending()
            }
        }.onFailure { throwable ->
            logService.error(throwable)
            baseTaskMetrics = null
            baseTaskConsecutive.clear()
            viewState.taskMetrics.postValue(null)
        }
    }

    private fun historyFilterForRange(
        taskId: Long = Entity.NO_ID,
        includeText: Boolean = true,
    ): TaskHistoryFilter {
        val textFilter = if (includeText) taskFilter.text else null

        if (selectedProgressRange == TaskProgressRange.ALL) {
            return TaskHistoryFilter(
                text = textFilter,
                taskId = taskId,
            )
        }

        val today = LocalDate.now()
        val startDate = selectedProgressRange.startDate(today)

        return TaskHistoryFilter(
            text = textFilter,
            initialDate = startDate.toStartOfDayCalendar(),
            finalDate = today.toEndOfDayCalendar(),
            taskId = taskId,
        )
    }

    private fun onClickUndoTask(taskId: Long) = viewModelScope.launch {
        try {
            cancelInlineFeedback(taskId)
            inlineTaskFeedbacks.remove(taskId)
            publishItemsWithInlineFeedback()
            publishTaskMetricsWithPending()
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskListViewAction.ShowErrorCompletingTask
        }
    }

    private suspend fun loadInlineMetrics(taskId: Long): TaskMetrics? = runCatching {
        withContext(dispatcherProvider.default()) {
            val filter = historyFilterForRange(taskId, includeText = true)
            getTaskMetricsUseCase(filter).getOrThrow()
        }
    }.onFailure { throwable ->
        logService.error(throwable)
    }.getOrNull()

    private fun publishItemsWithInlineFeedback(baseItems: List<UiModel>? = viewState.itemsView.value) {
        val updatedItems = applyInlineFeedbacks(baseItems.orEmpty())
        viewState.itemsView.postValue(updatedItems.toMutableList())
    }

    private fun applyInlineFeedbacks(items: List<UiModel>): List<UiModel> {
        if (inlineTaskFeedbacks.isEmpty()) {
            return items.map { item ->
                if (item is TaskUiModel) {
                    item.copy(
                        showInlineMetrics = false,
                        inlineMetrics = null,
                        inlineStatus = null,
                    )
                } else {
                    item
                }
            }
        }

        val itemsWithFeedback = items.toMutableList()

        inlineTaskFeedbacks.values.forEach { feedback ->
            val predictedMetrics = predictedTaskMetrics(feedback)
            val existingIndex =
                itemsWithFeedback.indexOfFirst { (it as? TaskUiModel)?.taskId == feedback.uiModel.taskId }

            val baseTaskUiModel =
                (itemsWithFeedback.getOrNull(existingIndex) as? TaskUiModel) ?: feedback.uiModel

            val inlineTaskUiModel =
                baseTaskUiModel.copy(
                    showInlineMetrics = true,
                    inlineMetrics = predictedMetrics,
                    inlineStatus = feedback.status,
                )

            if (existingIndex >= 0) {
                itemsWithFeedback[existingIndex] = inlineTaskUiModel
            }
        }

        return itemsWithFeedback
    }

    private fun publishTaskMetricsWithPending() {
        val pending = inlineTaskFeedbacks.values
        if (pending.isEmpty() && baseTaskMetrics == null) {
            viewState.taskMetrics.postValue(null)
            return
        }

        var doneTasks = baseTaskMetrics?.doneTasks ?: 0
        var notDoneTasks = baseTaskMetrics?.notDoneTasks ?: 0
        var consecutiveDone = baseTaskMetrics?.consecutiveDone ?: 0

        pending.forEach { feedback ->
            when (feedback.status) {
                TaskStatus.DONE -> {
                    doneTasks += 1
                    consecutiveDone += 1
                }

                TaskStatus.NOT_DONE -> {
                    notDoneTasks += 1
                    val baseConsecutive = baseTaskConsecutive[feedback.uiModel.taskId] ?: 0
                    consecutiveDone -= baseConsecutive
                }
            }
        }

        viewState.taskMetrics.postValue(
            TaskMetrics(
                doneTasks = doneTasks,
                notDoneTasks = notDoneTasks,
                consecutiveDone = consecutiveDone,
            ),
        )
    }

    private fun scheduleInlineFeedbackCommit(taskId: Long) {
        inlineTaskJobs[taskId]?.cancel()

        val job =
            viewModelScope.launch {
                delay(TASK_SWIPE_DELAY_IN_MILLIS)
                commitPendingSwipe(taskId)
            }

        inlineTaskJobs[taskId] = job
        job.invokeOnCompletion { inlineTaskJobs.remove(taskId) }
    }

    private suspend fun commitPendingSwipe(taskId: Long) {
        val feedback = inlineTaskFeedbacks[taskId] ?: return

        runCatching {
            withContext(dispatcherProvider.io()) {
                completeTaskUseCase(feedback.uiModel.taskId, feedback.status).getOrThrow()
            }
            inlineTaskFeedbacks.remove(taskId)
            removeTaskFromList(feedback.uiModel.taskId)
            loadTasks()
            updateTodayProgress(feedback.status)
            loadTaskMetrics()
            publishTaskMetricsWithPending()
        }.onFailure { throwable ->
            logService.error(throwable)
            viewState.viewAction.postValue(TaskListViewAction.ShowErrorCompletingTask)
            publishItemsWithInlineFeedback()
            publishTaskMetricsWithPending()
        }
    }

    private fun predictedTaskMetrics(feedback: InlineTaskFeedback): TaskMetrics {
        val baseConsecutive = baseTaskConsecutive[feedback.uiModel.taskId] ?: 0
        val baseMetrics =
            feedback.metrics
                ?: TaskMetrics(
                    doneTasks = 0,
                    notDoneTasks = 0,
                    consecutiveDone = baseConsecutive,
                )

        return when (feedback.status) {
            TaskStatus.DONE ->
                baseMetrics.copy(
                    doneTasks = baseMetrics.doneTasks + 1,
                    consecutiveDone = baseMetrics.consecutiveDone + 1,
                )

            TaskStatus.NOT_DONE ->
                baseMetrics.copy(
                    notDoneTasks = baseMetrics.notDoneTasks + 1,
                    consecutiveDone = 0,
                )
        }
    }

    private fun removeTaskFromList(taskId: Long) {
        val itemsView = viewState.itemsView.value?.toMutableList() ?: mutableListOf()
        val position = itemsView.indexOfFirst { (it as? TaskUiModel)?.taskId == taskId }

        if (position >= 0) {
            itemsView.removeAt(position)
        }

        currentTasks = currentTasks.filterNot { it.id == taskId }
        publishItemsWithInlineFeedback(itemsView)
    }

    private fun cancelInlineFeedback(taskId: Long) {
        inlineTaskJobs[taskId]?.cancel()
        inlineTaskJobs.remove(taskId)
        inlineTaskFeedbacks.remove(taskId)
    }

    private fun onSwipeTask(
        taskId: Long,
        status: TaskStatus,
    ) {
        viewModelScope.launch {
            try {
                val itemsView = viewState.itemsView.value ?: return@launch
                val taskUiModel =
                    itemsView.firstOrNull { (it as? TaskUiModel)?.taskId == taskId } as? TaskUiModel
                        ?: return@launch

                cancelInlineFeedback(taskUiModel.taskId)

                val inlineMetrics = loadInlineMetrics(taskUiModel.taskId)

                inlineTaskFeedbacks[taskUiModel.taskId] =
                    InlineTaskFeedback(
                        uiModel = taskUiModel,
                        metrics = inlineMetrics,
                        status = status,
                    )

                publishItemsWithInlineFeedback(itemsView)
                publishTaskMetricsWithPending()
                scheduleInlineFeedbackCommit(taskUiModel.taskId)
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskListViewAction.ShowErrorCompletingTask
            }
        }
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

    private fun currentProgressCacheKey() = ProgressCacheKey(
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
            val notDoneCount = if (status == TaskStatus.NOT_DONE) 1 else 0
            return listOf(
                TaskProgressDay(
                    date = today,
                    doneCount = doneCount,
                    notDoneCount = notDoneCount,
                    totalCount = 1,
                ),
            )
        }

        val existingIndex = days.indexOfFirst { it.date == today }
        val doneDelta = if (status == TaskStatus.DONE) 1 else 0
        val notDoneDelta = if (status == TaskStatus.NOT_DONE) 1 else 0

        val updatedDay =
            if (existingIndex >= 0) {
                val current = days[existingIndex]
                current.copy(
                    doneCount = current.doneCount + doneDelta,
                    notDoneCount = current.notDoneCount + notDoneDelta,
                    totalCount = current.totalCount + 1,
                )
            } else {
                TaskProgressDay(
                    date = today,
                    doneCount = doneDelta,
                    notDoneCount = notDoneDelta,
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

private data class InlineTaskFeedback(
    val uiModel: TaskUiModel,
    val metrics: TaskMetrics?,
    val status: TaskStatus,
)
