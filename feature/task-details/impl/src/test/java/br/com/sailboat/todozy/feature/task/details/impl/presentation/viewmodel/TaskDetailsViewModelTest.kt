package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.details.impl.presentation.factory.TaskDetailsUiModelFactory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
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
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class TaskDetailsViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val getTaskUseCase: GetTaskUseCase = mockk(relaxed = true)
    private val disableTaskUseCase: DisableTaskUseCase = mockk(relaxed = true)
    private val taskDetailsUiModelFactory: TaskDetailsUiModelFactory = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)

    private val viewModel = TaskDetailsViewModel(
        getTaskMetricsUseCase = getTaskMetricsUseCase,
        getTaskUseCase = getTaskUseCase,
        disableTaskUseCase = disableTaskUseCase,
        taskDetailsUiModelFactory = taskDetailsUiModelFactory,
        logService = logService,
    )

    @Test
    fun `should call getTaskUseCase when dispatchViewAction is called with OnStart`() {
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
        prepareScenario(taskDetailsResult = taskDetails)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 1) { getTaskUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
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
        prepareScenario(
            taskResult = Result.success(
                TaskMockFactory.makeTask().copy(alarm = null)
            )
        )

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
    }

    @Test
    fun `should call ShowErrorLoadingTaskDetails when getTaskViewUseCase returns error when dispatchViewAction is called with OnStart`() {
        val observer = mockk<Observer<TaskDetailsViewState.Action>>()
        val slot = slot<TaskDetailsViewState.Action>()
        val list = arrayListOf<TaskDetailsViewState.Action>()
        val taskId = 42L
        val taskResult: Result<Task> = Result.failure(Exception())
        viewModel.viewState.action.observeForever(observer)
        every { observer.onChanged(capture(slot)) } answers {
            list.add(slot.captured)
        }
        prepareScenario(taskResult = taskResult)

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))

        val expected = TaskDetailsViewState.Action.ShowErrorLoadingTaskDetails
        assertTrue { list.contains(expected) }
    }

    @Test
    fun `should call CloseTaskDetails(success = false) when getTaskUseCase returns error when dispatchViewAction is called with OnStart`() {
        val observer = mockk<Observer<TaskDetailsViewState.Action>>()
        val slot = slot<TaskDetailsViewState.Action>()
        val list = arrayListOf<TaskDetailsViewState.Action>()
        val taskId = 42L
        val taskResult: Result<Task> = Result.failure(Exception())
        viewModel.viewState.action.observeForever(observer)
        every { observer.onChanged(capture(slot)) } answers {
            list.add(slot.captured)
        }
        prepareScenario(taskResult = taskResult)

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
    }

    @Test
    fun `should navigate to task form when dispatchViewAction is called with OnClickEditTask`() {
        val taskId = 42L
        prepareScenario()

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnStart(taskId))
        viewModel.dispatchViewAction(TaskDetailsViewAction.OnClickEditTask)

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
    fun `should call getTaskUseCase when dispatchViewAction is called with OnReturnToDetails`() {
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
        prepareScenario(taskDetailsResult = taskDetails)
        viewModel.viewState.taskId = taskId

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 1) { getTaskUseCase(taskId) }
        assertEquals(taskDetails, viewModel.viewState.taskDetails.value)
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
        prepareScenario(
            taskResult = Result.success(
                TaskMockFactory.makeTask().copy(alarm = null)
            )
        )

        viewModel.dispatchViewAction(TaskDetailsViewAction.OnReturnToDetails)

        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
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
                )
            ),
        taskResult: Result<Task> = Result.success(TaskMockFactory.makeTask()),
        taskMetricsResult: Result<TaskMetrics> = Result.success(
            TaskMetrics(
                doneTasks = 15,
                notDoneTasks = 2,
                consecutiveDone = 5,
            )
        )
    ) {
        coEvery { getTaskUseCase(any()) } returns taskResult
        coEvery { taskDetailsUiModelFactory.create(any()) } returns taskDetailsResult
        coEvery { getTaskMetricsUseCase(any()) } returns taskMetricsResult
    }
}
