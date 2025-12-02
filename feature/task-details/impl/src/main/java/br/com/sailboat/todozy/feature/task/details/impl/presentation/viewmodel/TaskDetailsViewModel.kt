package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
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
import br.com.sailboat.todozy.utility.kotlin.extension.toEndOfDayCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toStartOfDayCalendar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar

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
    private var currentAlarm: Alarm? = null
    private var progressJob: Job? = null
    private val progressCache: MutableMap<TaskProgressRange, List<TaskProgressDay>> = mutableMapOf()

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
            progressCache.clear()
            currentAlarm = null

            val task = getTaskUseCase(viewState.taskId).getOrThrow()
            val taskDetails = taskDetailsUiModelFactory.create(task)
            currentAlarm = task.alarm
            viewState.taskProgressDayOrder.postValue(dayOrderForAlarm(task.alarm))

            viewState.taskDetails.postValue(taskDetails)

            shouldShowProgress = false
            val taskMetrics: TaskMetrics? =
                task.alarm?.run {
                    shouldShowProgress = RepeatType.isAlarmRepeating(this)
                    if (shouldShowProgress) {
                        val taskMetrics = getTaskMetricsUseCase(historyFilterForRange())
                        return@run taskMetrics.getOrNull()
                    }
                    shouldShowProgress = false
                    return@run null
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
        loadTaskMetrics()
    }

    private fun loadProgress(force: Boolean = false) {
        val shouldFetch = force || shouldShowProgress
        if (shouldFetch.not()) {
            viewState.taskProgressDays.postValue(emptyList())
            viewState.taskProgressLoading.postValue(false)
            return
        }

        val cachedProgress = progressCache[selectedProgressRange]
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

                    val progress =
                        withContext(dispatcherProvider.default()) {
                            getTaskProgressUseCase(
                                TaskProgressFilter(
                                    range = selectedProgressRange,
                                    taskId = viewState.taskId,
                                ),
                            ).getOrThrow()
                        }
                    val filteredProgress = filterProgressForAlarm(progress)
                    progressCache[selectedProgressRange] = filteredProgress
                    viewState.taskProgressRange.postValue(selectedProgressRange)
                    viewState.taskProgressDays.postValue(filteredProgress)
                } catch (throwable: Exception) {
                    logService.error(throwable)
                    viewState.taskProgressDays.postValue(emptyList())
                } finally {
                    viewState.taskProgressLoading.postValue(false)
                }
            }
    }

    private fun loadTaskMetrics() = viewModelScope.launch {
        if (shouldShowProgress.not()) {
            viewState.taskMetrics.postValue(null)
            return@launch
        }

        runCatching {
            val filter = historyFilterForRange()
            getTaskMetricsUseCase(filter).getOrThrow()
        }.onSuccess { metrics ->
            viewState.taskMetrics.postValue(metrics)
        }.onFailure { throwable ->
            logService.error(throwable)
            viewState.taskMetrics.postValue(null)
        }
    }

    private fun historyFilterForRange(): TaskHistoryFilter {
        if (selectedProgressRange == TaskProgressRange.ALL) {
            return TaskHistoryFilter(taskId = viewState.taskId)
        }

        val today = LocalDate.now()
        val startDate = selectedProgressRange.startDate(today)

        return TaskHistoryFilter(
            taskId = viewState.taskId,
            initialDate = startDate.toStartOfDayCalendar(),
            finalDate = today.toEndOfDayCalendar(),
        )
    }

    private fun dayOrderForAlarm(alarm: Alarm?): List<DayOfWeek> {
        return when (alarm?.repeatType) {
            RepeatType.CUSTOM -> {
                val allowedDays = parseCustomDays(alarm.customDays)
                if (allowedDays.isEmpty()) {
                    TASK_PROGRESS_FULL_WEEK_ORDER
                } else {
                    TASK_PROGRESS_FULL_WEEK_ORDER.filter { allowedDays.contains(it) }
                }
            }
            RepeatType.WEEK -> {
                val dayOfWeek = calendarDayToDayOfWeek(alarm.dateTime.get(Calendar.DAY_OF_WEEK))
                dayOfWeek?.let { listOf(it) } ?: TASK_PROGRESS_FULL_WEEK_ORDER
            }
            RepeatType.DAY, RepeatType.MINUTE, RepeatType.HOUR, RepeatType.SECOND -> TASK_PROGRESS_FULL_WEEK_ORDER
            RepeatType.MONTH, RepeatType.YEAR -> TASK_PROGRESS_FULL_WEEK_ORDER
            else -> TASK_PROGRESS_FULL_WEEK_ORDER
        }
    }

    private fun filterProgressForAlarm(progress: List<TaskProgressDay>): List<TaskProgressDay> {
        if (shouldShowProgress.not()) {
            return progress
        }

        val predicate = progressPredicateForAlarm(currentAlarm) ?: return progress
        return progress.filter { predicate(it.date) }
    }

    private fun progressPredicateForAlarm(alarm: Alarm?): ((LocalDate) -> Boolean)? {
        return when (alarm?.repeatType) {
            RepeatType.CUSTOM -> {
                val allowedDays = parseCustomDays(alarm.customDays)
                if (allowedDays.isEmpty()) {
                    null
                } else {
                    { date: LocalDate -> allowedDays.contains(date.dayOfWeek) }
                }
            }
            RepeatType.WEEK -> {
                val dayOfWeek = calendarDayToDayOfWeek(alarm.dateTime.get(Calendar.DAY_OF_WEEK))
                dayOfWeek?.let { expected -> { date: LocalDate -> date.dayOfWeek == expected } }
            }
            RepeatType.MONTH -> {
                val dayOfMonth = alarm.dateTime.get(Calendar.DAY_OF_MONTH)
                ({ date: LocalDate -> date.dayOfMonth == dayOfMonth })
            }
            RepeatType.YEAR -> {
                val dayOfMonth = alarm.dateTime.get(Calendar.DAY_OF_MONTH)
                val month = alarm.dateTime.get(Calendar.MONTH) + 1
                ({ date: LocalDate -> date.dayOfMonth == dayOfMonth && date.monthValue == month })
            }
            RepeatType.DAY, RepeatType.MINUTE, RepeatType.HOUR, RepeatType.SECOND -> null
            else -> null
        }
    }

    private fun parseCustomDays(customDays: String?): Set<DayOfWeek> = customDays
        ?.mapNotNull { it.digitToIntOrNull() }
        ?.mapNotNull { dayId -> calendarDayToDayOfWeek(dayId) }
        ?.toSet()
        ?: emptySet()

    private fun calendarDayToDayOfWeek(dayId: Int): DayOfWeek? = when (dayId) {
        Calendar.SUNDAY -> DayOfWeek.SUNDAY
        Calendar.MONDAY -> DayOfWeek.MONDAY
        Calendar.TUESDAY -> DayOfWeek.TUESDAY
        Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
        Calendar.THURSDAY -> DayOfWeek.THURSDAY
        Calendar.FRIDAY -> DayOfWeek.FRIDAY
        Calendar.SATURDAY -> DayOfWeek.SATURDAY
        else -> null
    }
}
