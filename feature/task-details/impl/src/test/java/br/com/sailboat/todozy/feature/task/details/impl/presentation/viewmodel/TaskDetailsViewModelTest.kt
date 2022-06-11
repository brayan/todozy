package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.uicomponent.impl.helper.CoroutinesTestRule
import br.com.sailboat.uicomponent.model.AlarmUiModel
import br.com.sailboat.uicomponent.model.TitleUiModel
import br.com.sailboat.uicomponent.model.UiModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class TaskDetailsViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getTaskDetailsViewUseCase: GetTaskDetailsViewUseCase = mockk(relaxed = true)
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val getAlarmUseCase: GetAlarmUseCase = mockk(relaxed = true)
    private val getTaskUseCase: GetTaskUseCase = mockk(relaxed = true)
    private val disableTaskUseCase: DisableTaskUseCase = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)

    private val viewModel = TaskDetailsViewModel(
        getTaskDetailsViewUseCase = getTaskDetailsViewUseCase,
        getTaskMetricsUseCase = getTaskMetricsUseCase,
        getAlarmUseCase = getAlarmUseCase,
        getTaskUseCase = getTaskUseCase,
        disableTaskUseCase = disableTaskUseCase,
        logService = logService,
    )

    @Test
    fun `should call getTaskDetailsViewUseCase when dispatchViewAction is called with OnStart`() {
        val taskId = 42L
        val taskDetails = listOf(
            TitleUiModel(title = "Task Name"),
            AlarmUiModel(
                date = "07/03/2022",
                time = "11:55",
                description = "Today, March 7, 2022",
                isCustom = false,
                shouldRepeat = true,
                customDays = null,
            )
        )
        val taskDetailsResult = Result.success(taskDetails)
        prepareScenario(taskDetailsResult = taskDetailsResult)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 1) { getTaskDetailsViewUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
    }

    @Test
    fun `should call getAlarmUseCase when dispatchViewAction is called with OnStart`() {
        val taskId = 42L
        val alarmResult = Result.success(
            Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.WEEK,
            )
        )
        prepareScenario(alarmResult = alarmResult)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 1) { getAlarmUseCase(taskId) }
    }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnStart`() {
        val taskId = 42L
        val filter = TaskHistoryFilter(taskId = taskId)
        val taskMetrics = TaskMetrics(
            doneTasks = 15,
            notDoneTasks = 2,
            consecutiveDone = 5,
        )
        prepareScenario(taskMetricsResult = Result.success(taskMetrics))

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 1) { getTaskMetricsUseCase(filter) }
        assertEquals(taskMetrics, viewModel.viewState.taskMetrics.value)
    }

    @Test
    fun `should not call getTaskMetricsUseCase when dispatchViewAction is called with OnStart when alarm is null`() {
        val taskId = 42L
        prepareScenario(alarmResult = Result.success(null))

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
    }

    @Test
    fun `should call ShowErrorLoadingTaskDetails when getTaskDetailsViewUseCase returns error when dispatchViewAction is called with OnStart`() {
        val observer = mockk<Observer<TaskDetailsViewState.Action>>()
        val slot = slot<TaskDetailsViewState.Action>()
        val list = arrayListOf<TaskDetailsViewState.Action>()
        val taskId = 42L
        val taskDetailsResult: Result<List<UiModel>> = Result.failure(Exception())
        viewModel.viewState.action.observeForever(observer)
        every { observer.onChanged(capture(slot)) } answers {
            list.add(slot.captured)
        }
        prepareScenario(taskDetailsResult = taskDetailsResult)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        val expected = TaskDetailsViewState.Action.ShowErrorLoadingTaskDetails
        assertTrue { list.contains(expected) }
    }

    @Test
    fun `should call CloseTaskDetails(success = false) when getTaskDetailsViewUseCase returns error when dispatchViewAction is called with OnStart`() {
        val observer = mockk<Observer<TaskDetailsViewState.Action>>()
        val slot = slot<TaskDetailsViewState.Action>()
        val list = arrayListOf<TaskDetailsViewState.Action>()
        val taskId = 42L
        val taskDetailsResult: Result<List<UiModel>> = Result.failure(Exception())
        viewModel.viewState.action.observeForever(observer)
        every { observer.onChanged(capture(slot)) } answers {
            list.add(slot.captured)
        }
        prepareScenario(taskDetailsResult = taskDetailsResult)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        val expected = TaskDetailsViewState.Action.CloseTaskDetails(success = false)
        assertTrue { list.contains(expected) }
    }

    @Test
    fun `should confirm if user wants to delete an task when dispatchViewAction is called with OnClickMenuDelete`() {
        prepareScenario()

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickMenuDelete)

        val expected = TaskDetailsViewState.Action.ConfirmDeleteTask
        assertEquals(expected, viewModel.viewState.action.value)
    }

    @Test
    fun `should call disableTaskUseCase when dispatchViewAction is called with OnClickConfirmDeleteTask`() {
        val task = TaskMockFactory.makeTask()
        prepareScenario(taskResult = Result.success(task))

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickConfirmDeleteTask)

        coVerify(exactly = 1) { disableTaskUseCase(task) }
    }

    @Test
    fun `should close task details when dispatchViewAction is called with OnClickConfirmDeleteTask`() {
        prepareScenario()

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickConfirmDeleteTask)

        val expected = TaskDetailsViewState.Action.CloseTaskDetails(success = true)
        assertEquals(expected, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task form when dispatchViewAction is called with OnClickEditTask`() {
        val taskId = 42L
        prepareScenario()

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))
        viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickEditTask)

        val expected = TaskDetailsViewState.Action.NavigateToTaskForm(taskId)
        assertEquals(expected, viewModel.viewState.action.value)
    }

    @Test
    fun `should call getTaskDetailsViewUseCase when dispatchViewAction is called with OnReturnToDetails`() {
        val taskId = 42L
        val taskDetails = listOf(
            TitleUiModel(title = "Task Name"),
            AlarmUiModel(
                date = "07/03/2022",
                time = "11:55",
                description = "Today, March 7, 2022",
                isCustom = false,
                shouldRepeat = true,
                customDays = null,
            )
        )
        val taskDetailsResult = Result.success(taskDetails)
        prepareScenario(taskDetailsResult = taskDetailsResult)
        viewModel.viewState.taskId = taskId

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 1) { getTaskDetailsViewUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
    }

    @Test
    fun `should call getAlarmUseCase when dispatchViewAction is called with OnReturnToDetails`() {
        val taskId = 42L
        val alarmResult = Result.success(
            Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.WEEK,
            )
        )
        viewModel.viewState.taskId = taskId
        prepareScenario(alarmResult = alarmResult)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 1) { getAlarmUseCase(taskId) }
    }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnReturnToDetails`() {
        val taskId = 42L
        val filter = TaskHistoryFilter(taskId = taskId)
        val taskMetrics = TaskMetrics(
            doneTasks = 15,
            notDoneTasks = 2,
            consecutiveDone = 5,
        )
        viewModel.viewState.taskId = taskId
        prepareScenario(taskMetricsResult = Result.success(taskMetrics))

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 1) { getTaskMetricsUseCase(filter) }
        assertEquals(taskMetrics, viewModel.viewState.taskMetrics.value)
    }

    @Test
    fun `should not call getTaskMetricsUseCase when dispatchViewAction is called with OnReturnToDetails when alarm is null`() {
        val taskId = 42L
        viewModel.viewState.taskId = taskId
        prepareScenario(alarmResult = Result.success(null))

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
    }

    private fun prepareScenario(
        taskDetailsResult: Result<List<UiModel>> =
            Result.success(
                listOf(
                    TitleUiModel(title = "Task Name"),
                    AlarmUiModel(
                        date = "07/03/2022",
                        time = "11:55",
                        description = "Today, March 7, 2022",
                        isCustom = false,
                        shouldRepeat = true,
                        customDays = null,
                    )
                )
            ),
        taskResult: Result<Task> = Result.success(TaskMockFactory.makeTask()),
        alarmResult: Result<Alarm?> = Result.success(
            Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.WEEK,
            )
        ),
        taskMetricsResult: Result<TaskMetrics> = Result.success(
            TaskMetrics(
                doneTasks = 15,
                notDoneTasks = 2,
                consecutiveDone = 5,
            )
        )
    ) {
        coEvery { getTaskDetailsViewUseCase(any()) } returns taskDetailsResult
        coEvery { getTaskUseCase(any()) } returns taskResult
        coEvery { getAlarmUseCase(any()) } returns alarmResult
        coEvery { getTaskMetricsUseCase(any()) } returns taskMetricsResult
    }

}