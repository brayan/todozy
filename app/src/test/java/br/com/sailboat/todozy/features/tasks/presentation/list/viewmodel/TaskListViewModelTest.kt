package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sailboat.todozy.features.tasks.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarmUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksViewUseCase
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class TaskListViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    private val getTasksViewUseCase: GetTasksViewUseCase = mockk(relaxed = true)
    private val getAlarmUseCase: GetAlarmUseCase = mockk(relaxed = true)
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase = mockk(relaxed = true)
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val completeTaskUseCase: CompleteTaskUseCase = mockk(relaxed = true)

    private val viewModel = TaskListViewModel(
        getTasksViewUseCase = getTasksViewUseCase,
        getAlarmUseCase = getAlarmUseCase,
        scheduleAllAlarmsUseCase = scheduleAllAlarmsUseCase,
        getTaskMetricsUseCase = getTaskMetricsUseCase,
        completeTaskUseCase = completeTaskUseCase,
    )

    @Test
    fun `should navigate to about screen when dispatchViewAction is called with OnClickMenuAbout`() {
        viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuAbout)

        assertEquals(TaskListViewState.Action.NavigateToAbout, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to history screen when dispatchViewAction is called with OnClickMenuHistory`() {
        viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuHistory)

        assertEquals(TaskListViewState.Action.NavigateToHistory, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to settings screen when dispatchViewAction is called with OnClickMenuSettings`() {
        viewModel.dispatchViewAction(TaskListViewAction.OnClickMenuSettings)

        assertEquals(TaskListViewState.Action.NavigateToSettings, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task form screen when dispatchViewAction is called with OnClickNewTask`() {
        viewModel.dispatchViewAction(TaskListViewAction.OnClickNewTask)

        assertEquals(TaskListViewState.Action.NavigateToTaskForm, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task details screen when dispatchViewAction is called with OnClickTask`() {
        val taskId = 123L

        viewModel.dispatchViewAction(TaskListViewAction.OnClickTask(taskId = taskId))

        val expected = TaskListViewState.Action.NavigateToTaskDetails(taskId = taskId)
        assertEquals(expected, viewModel.viewState.action.value)
    }

}