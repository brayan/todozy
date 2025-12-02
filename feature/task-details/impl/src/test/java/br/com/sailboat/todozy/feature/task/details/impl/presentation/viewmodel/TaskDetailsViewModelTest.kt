package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.factory.TaskDetailsUiModelFactory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter
import br.com.sailboat.todozy.feature.task.history.domain.usecase.GetTaskProgressUseCase
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.uicomponent.impl.helper.CoroutinesTestRule
import br.com.sailboat.uicomponent.model.AlarmUiModel
import br.com.sailboat.uicomponent.model.TitleUiModel
import br.com.sailboat.uicomponent.model.UiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class TaskDetailsViewModelTest {
    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val getTaskProgressUseCase: GetTaskProgressUseCase = mockk(relaxed = true)
    private val getTaskUseCase: GetTaskUseCase = mockk(relaxed = true)
    private val disableTaskUseCase: DisableTaskUseCase = mockk(relaxed = true)
    private val taskDetailsUiModelFactory: TaskDetailsUiModelFactory = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)

    private val viewModel =
        TaskDetailsViewModel(
            getTaskMetricsUseCase = getTaskMetricsUseCase,
            getTaskProgressUseCase = getTaskProgressUseCase,
            getTaskUseCase = getTaskUseCase,
            disableTaskUseCase = disableTaskUseCase,
            taskDetailsUiModelFactory = taskDetailsUiModelFactory,
            logService = logService,
            dispatcherProvider = coroutinesTestRule.dispatcherProvider,
        )

    @Test
    fun `should call getTaskUseCase when dispatchViewAction is called with OnStart`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 42L
        val taskDetails =
            listOf(
                TitleUiModel(title = "Task Name"),
                AlarmUiModel(
                    date = "07/03/2022",
                    time = "11:55",
                    description = "Today, March 7, 2022",
                    isCustom = false,
                    shouldRepeat = true,
                    customDays = null,
                ),
            )
        prepareScenario(taskDetailsResult = taskDetails)

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
        advanceUntilIdle()

        coVerify(exactly = 1) { getTaskUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
    }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnStart`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 42L
        val taskMetrics =
            TaskMetrics(
                doneTasks = 15,
                notDoneTasks = 2,
                consecutiveDone = 5,
            )
        prepareScenario(taskMetricsResult = Result.success(taskMetrics))

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
        advanceUntilIdle()

        coVerify(exactly = 1) {
            getTaskMetricsUseCase(
                withArg { filter ->
                    assertEquals(taskId, filter.taskId)
                    assertNotNull(filter.initialDate)
                    assertNotNull(filter.finalDate)
                },
            )
        }
        assertEquals(taskMetrics, viewModel.viewState.taskMetrics.value)
    }

    @Test
    fun `should call getTaskProgressUseCase when dispatchViewAction is called with OnStart`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 42L
        prepareScenario()

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
        advanceUntilIdle()

        coVerify { getTaskProgressUseCase(TaskProgressFilter(TaskProgressRange.LAST_YEAR, taskId)) }
    }

    @Test
    fun `should not call getTaskMetricsUseCase when dispatchViewAction is called with OnStart when alarm is null`() =
        runTest(coroutinesTestRule.dispatcher) {
            val taskId = 42L
            prepareScenario(
                taskResult =
                    Result.success(
                        TaskMockFactory.makeTask().copy(alarm = null),
                    ),
            )

            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
            advanceUntilIdle()

            coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
            coVerify(exactly = 0) { getTaskProgressUseCase(any()) }
        }

    @Test
    fun `should call ShowErrorLoadingTaskDetails when getTaskViewUseCase returns error when dispatchViewAction is called with OnStart`() =
        runTest(coroutinesTestRule.dispatcher) {
            val observer = mockk<Observer<TaskDetailsViewAction>>()
            val slot = slot<TaskDetailsViewAction>()
            val list = arrayListOf<TaskDetailsViewAction>()
            val taskId = 42L
            val taskResult: Result<Task> = Result.failure(Exception())
            viewModel.viewState.viewAction.observeForever(observer)
            every { observer.onChanged(capture(slot)) } answers {
                list.add(slot.captured)
            }
            prepareScenario(taskResult = taskResult)

            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
            advanceUntilIdle()

            val expected = TaskDetailsViewAction.ShowErrorLoadingTaskDetails
            assertTrue { list.contains(expected) }
        }

    @Test
    fun `should call CloseTaskDetails(success = false) when getTaskUseCase returns error when dispatchViewAction is called with OnStart`() =
        runTest(coroutinesTestRule.dispatcher) {
            val observer = mockk<Observer<TaskDetailsViewAction>>()
            val slot = slot<TaskDetailsViewAction>()
            val list = arrayListOf<TaskDetailsViewAction>()
            val taskId = 42L
            val taskResult: Result<Task> = Result.failure(Exception())
            viewModel.viewState.viewAction.observeForever(observer)
            every { observer.onChanged(capture(slot)) } answers {
                list.add(slot.captured)
            }
            prepareScenario(taskResult = taskResult)

            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
            advanceUntilIdle()

            val expected = TaskDetailsViewAction.CloseTaskDetails(success = false)
            assertTrue { list.contains(expected) }
        }

    @Test
    fun `should confirm if user wants to delete an task when dispatchViewAction is called with OnClickMenuDelete`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickMenuDelete)

        val expected = TaskDetailsViewAction.ConfirmDeleteTask
        assertEquals(expected, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should call disableTaskUseCase when dispatchViewAction is called with OnClickConfirmDeleteTask`() =
        runTest(coroutinesTestRule.dispatcher) {
            val task = TaskMockFactory.makeTask()
            prepareScenario(taskResult = Result.success(task))

            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickConfirmDeleteTask)
            advanceUntilIdle()

            coVerify(exactly = 1) { disableTaskUseCase(task) }
        }

    @Test
    fun `should close task details when dispatchViewAction is called with OnClickConfirmDeleteTask`() {
        prepareScenario()
    }

    @Test
    fun `should navigate to task form when dispatchViewAction is called with OnClickEditTask`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 42L
        prepareScenario()

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
        advanceUntilIdle()
        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnClickEditTask)

        val expected = TaskDetailsViewAction.NavigateToTaskForm(taskId)
        assertEquals(expected, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should call getTaskUseCase when dispatchViewAction is called with OnReturnToDetails`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 42L
        val taskDetails =
            listOf(
                TitleUiModel(title = "Task Name"),
                AlarmUiModel(
                    date = "07/03/2022",
                    time = "11:55",
                    description = "Today, March 7, 2022",
                    isCustom = false,
                    shouldRepeat = true,
                    customDays = null,
                ),
            )
        prepareScenario(taskDetailsResult = taskDetails)
        viewModel.viewState.taskId = taskId

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnReturnToDetails)
        advanceUntilIdle()

        coVerify(exactly = 1) { getTaskUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
    }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnReturnToDetails`() =
        runTest(coroutinesTestRule.dispatcher) {
            val taskId = 42L
            val filter = TaskHistoryFilter(taskId = taskId)
            val taskMetrics =
                TaskMetrics(
                    doneTasks = 15,
                    notDoneTasks = 2,
                    consecutiveDone = 5,
                )
            viewModel.viewState.taskId = taskId
            prepareScenario(taskMetricsResult = Result.success(taskMetrics))

            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnReturnToDetails)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                getTaskMetricsUseCase(
                    withArg { filter ->
                        assertEquals(taskId, filter.taskId)
                        assertNotNull(filter.initialDate)
                        assertNotNull(filter.finalDate)
                    },
                )
            }
            assertEquals(taskMetrics, viewModel.viewState.taskMetrics.value)
        }

    @Test
    fun `should reload progress when dispatchViewAction is called with OnSelectProgressRange`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 42L
        viewModel.viewState.taskId = taskId
        prepareScenario()

        viewModel.dispatchViewIntent(
            TaskDetailsViewIntent.OnSelectProgressRange(TaskProgressRange.LAST_30_DAYS),
        )
        advanceUntilIdle()

        coVerify {
            getTaskProgressUseCase(TaskProgressFilter(TaskProgressRange.LAST_30_DAYS, taskId))
        }
        assertEquals(TaskProgressRange.LAST_30_DAYS, viewModel.viewState.taskProgressRange.value)
    }

    @Test
    fun `should show progress only for scheduled repeat days`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 7L
        val startDate = LocalDate.of(2024, 8, 12) // Monday
        val progressDays =
            (0..6).map { offset ->
                val date = startDate.plusDays(offset.toLong())
                TaskProgressDay(
                    date = date,
                    doneCount = if (date.dayOfWeek == DayOfWeek.MONDAY) 1 else 0,
                    notDoneCount = if (date.dayOfWeek == DayOfWeek.TUESDAY) 1 else 0,
                    totalCount =
                        when (date.dayOfWeek) {
                            DayOfWeek.MONDAY, DayOfWeek.TUESDAY -> 1
                            else -> 0
                        },
                )
            }
        val alarmCalendar =
            Calendar.getInstance().apply {
                set(2024, Calendar.AUGUST, 12, 8, 0, 0)
            }
        val repeatingAlarm =
            Alarm(
                dateTime = alarmCalendar,
                repeatType = RepeatType.CUSTOM,
                customDays = "${Calendar.MONDAY}${Calendar.WEDNESDAY}${Calendar.FRIDAY}",
            )

        prepareScenario(
            taskResult = Result.success(TaskMockFactory.makeTask(id = taskId, alarm = repeatingAlarm)),
            taskProgressResult = Result.success(progressDays),
        )

        viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnStart(taskId))
        advanceUntilIdle()

        val expectedDayOrder =
            listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
            )
        assertEquals(expectedDayOrder, viewModel.viewState.taskProgressDayOrder.value)
        val expectedProgress = progressDays.filter { it.date.dayOfWeek in expectedDayOrder }
        assertEquals(expectedProgress, viewModel.viewState.taskProgressDays.value)
    }

    @Test
    fun `should not call getTaskMetricsUseCase when dispatchViewAction is called with OnReturnToDetails when alarm is null`() =
        runTest(coroutinesTestRule.dispatcher) {
            val taskId = 42L
            viewModel.viewState.taskId = taskId
            prepareScenario(
                taskResult =
                    Result.success(
                        TaskMockFactory.makeTask().copy(alarm = null),
                    ),
            )

            viewModel.dispatchViewIntent(TaskDetailsViewIntent.OnReturnToDetails)
            advanceUntilIdle()

            coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
            coVerify(exactly = 0) { getTaskProgressUseCase(any()) }
        }

    private fun prepareScenario(
        taskDetailsResult: List<UiModel> =
            listOf(
                TitleUiModel(title = "Task Name"),
                AlarmUiModel(
                    date = "07/03/2022",
                    time = "11:55",
                    description = "Today, March 7, 2022",
                    isCustom = false,
                    shouldRepeat = true,
                    customDays = null,
                ),
            ),
        taskResult: Result<Task> = Result.success(TaskMockFactory.makeTask()),
        taskMetricsResult: Result<TaskMetrics> =
            Result.success(
                TaskMetrics(
                    doneTasks = 15,
                    notDoneTasks = 2,
                    consecutiveDone = 5,
                ),
            ),
        taskProgressResult: Result<List<TaskProgressDay>> = Result.success(emptyList()),
    ) {
        coEvery { getTaskUseCase(any()) } returns taskResult
        coEvery { taskDetailsUiModelFactory.create(any()) } returns taskDetailsResult
        coEvery { getTaskMetricsUseCase(any()) } returns taskMetricsResult
        coEvery { getTaskProgressUseCase(any()) } returns taskProgressResult
    }
}
