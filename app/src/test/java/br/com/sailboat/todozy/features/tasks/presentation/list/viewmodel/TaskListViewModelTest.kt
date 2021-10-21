package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sailboat.todozy.TestCoroutineRule
import br.com.sailboat.todozy.core.platform.LogService
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.TaskItemView
import br.com.sailboat.todozy.features.tasks.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarmUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksViewUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TaskListViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getTasksViewUseCase: GetTasksViewUseCase = mockk(relaxed = true)
    private val getAlarmUseCase: GetAlarmUseCase = mockk(relaxed = true)
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase = mockk(relaxed = true)
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val completeTaskUseCase: CompleteTaskUseCase = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)

    private val viewModel = TaskListViewModel(
        getTasksViewUseCase = getTasksViewUseCase,
        getAlarmUseCase = getAlarmUseCase,
        scheduleAllAlarmsUseCase = scheduleAllAlarmsUseCase,
        getTaskMetricsUseCase = getTaskMetricsUseCase,
        completeTaskUseCase = completeTaskUseCase,
        logService = logService,
    )

    @Test
    fun `should call getTasksViewUseCase when dispatchViewAction is called with OnStart`() {
        runBlocking {
            val tasks = mutableListOf<ItemView>(TaskItemView(taskId = 543L, taskName = "Task 543"))
            prepareScenario(tasksResult = tasks)

            viewModel.dispatchViewAction(TaskListViewAction.OnStart)

            coVerify(exactly = 1) { getTasksViewUseCase(search = "") }
            assertEquals(tasks, viewModel.viewState.itemsView.value)
        }
    }

    @Test
    fun `should call scheduleAllAlarmsUseCase when dispatchViewAction is called with OnStart`() {
        runBlocking {
            prepareScenario()

            viewModel.dispatchViewAction(TaskListViewAction.OnStart)

            coVerify(exactly = 1) { scheduleAllAlarmsUseCase() }
        }
    }

    @Test
    fun `should navigate to about screen when dispatchViewAction is called with OnClickMenuAbout`() {
        prepareScenario()

        viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuAbout)

        assertEquals(TaskListViewState.Action.NavigateToAbout, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to history screen when dispatchViewAction is called with OnClickMenuHistory`() {
        prepareScenario()

        viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuHistory)

        assertEquals(TaskListViewState.Action.NavigateToHistory, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to settings screen when dispatchViewAction is called with OnClickMenuSettings`() {
        prepareScenario()

        viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuSettings)

        assertEquals(TaskListViewState.Action.NavigateToSettings, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task form screen when dispatchViewAction is called with OnClickNewTask`() {
        prepareScenario()
        viewModel.dispatchViewAction(TaskListViewAction.OnClickNewTask)

        assertEquals(TaskListViewState.Action.NavigateToTaskForm, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task details screen when dispatchViewAction is called with OnClickTask`() {
        prepareScenario()
        val taskId = 123L

        viewModel.dispatchViewAction(TaskListViewAction.OnClickTask(taskId = taskId))

        val expected = TaskListViewState.Action.NavigateToTaskDetails(taskId = taskId)
        assertEquals(expected, viewModel.viewState.action.value)
    }

    private fun prepareScenario(
        tasksResult: List<ItemView> = listOf(
            TaskItemView(
                taskName = "Task Name",
                taskId = 123L,
            )
        )
    ) {
        coEvery { getTasksViewUseCase(any()) } returns tasksResult
        coEvery { scheduleAllAlarmsUseCase() } just runs
    }

}