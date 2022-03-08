package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sailboat.todozy.domain.model.*
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.GetTaskDetailsViewUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.uicomponent.helper.CoroutinesTestRule
import br.com.sailboat.todozy.uicomponent.model.AlarmUiModel
import br.com.sailboat.todozy.uicomponent.model.TitleUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

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
        prepareScenario(taskDetails = taskDetails)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 1) { getTaskDetailsViewUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
    }

    @Test
    fun `should call getAlarmUseCase when dispatchViewAction is called with OnStart`() {
        val taskId = 42L
        val alarmResult = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.WEEK,
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
        prepareScenario(taskMetrics = taskMetrics)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 1) { getTaskMetricsUseCase(filter) }
        assertEquals(taskMetrics, viewModel.viewState.taskMetrics.value)
    }

    @Test
    fun `should not call getTaskMetricsUseCase when dispatchViewAction is called with OnStart when alarm is null`() {
        val taskId = 42L
        prepareScenario(alarmResult = null)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
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
        prepareScenario(taskDetails = taskDetails)
        viewModel.viewState.taskId = taskId

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 1) { getTaskDetailsViewUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
    }

    @Test
    fun `should call getAlarmUseCase when dispatchViewAction is called with OnReturnToDetails`() {
        val taskId = 42L
        val alarmResult = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.WEEK,
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
        prepareScenario(taskMetrics = taskMetrics)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 1) { getTaskMetricsUseCase(filter) }
        assertEquals(taskMetrics, viewModel.viewState.taskMetrics.value)
    }

    @Test
    fun `should not call getTaskMetricsUseCase when dispatchViewAction is called with OnReturnToDetails when alarm is null`() {
        val taskId = 42L
        viewModel.viewState.taskId = taskId
        prepareScenario(alarmResult = null)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
    }

    private fun prepareScenario(
        taskDetails: List<UiModel> = listOf(
            TitleUiModel(title = "Task Name"),
            AlarmUiModel(
                date = "07/03/2022",
                time = "11:55",
                description = "Today, March 7, 2022",
                isCustom = false,
                shouldRepeat = true,
                customDays = null,
            )
        ),
        taskResult: Result<Task> = Result.success(TaskMockFactory.makeTask()),
        alarmResult: Alarm? = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.WEEK,
        ),
        taskMetrics: TaskMetrics = TaskMetrics(
            doneTasks = 15,
            notDoneTasks = 2,
            consecutiveDone = 5,
        )
    ) {
        coEvery { getTaskDetailsViewUseCase(any()) } returns taskDetails
        coEvery { getTaskUseCase(any()) } returns taskResult
        coEvery { getAlarmUseCase(any()) } returns alarmResult
        coEvery { getTaskMetricsUseCase(any()) } returns taskMetrics
    }

}