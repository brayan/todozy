package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.sailboat.todozy.TestCoroutineRule
import br.com.sailboat.todozy.core.platform.LogService
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarmUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksViewUseCase
import br.com.sailboat.todozy.uicomponent.model.UiModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
            val tasks = mutableListOf<UiModel>(TaskUiModel(taskId = 543L, taskName = "Task 543"))
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

    @Test
    fun `should call getTasksViewUseCase and search for tasks when dispatchViewAction is called with OnInputSearchTerm`() {
        runBlocking {
            val tasks = mutableListOf<UiModel>(TaskUiModel(taskId = 543L, taskName = "Task 543"))
            val term = "Term"
            prepareScenario(tasksResult = tasks)

            viewModel.dispatchViewAction(TaskListViewAction.OnInputSearchTerm(term = term))

            coVerify(exactly = 1) { getTasksViewUseCase(search = term) }
            assertEquals(tasks, viewModel.viewState.itemsView.value)
        }
    }

    @Test
    fun `should call completeTaskUseCase when dispatchViewAction is called with OnSwipeTask`() {
        testCoroutineRule.runBlockingTest {
            val tasks = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario()

            viewModel.dispatchViewAction(TaskListViewAction.OnSwipeTask(position, status))

            coVerify(exactly = 1) { completeTaskUseCase(taskId = 978L, status = status) }
        }
    }

    @Test
    fun `should update removed task when dispatchViewAction is called with OnSwipeTask`() {
        testCoroutineRule.runBlockingTest {
            val task1 = TaskUiModel(taskId = 543L, taskName = "Task 543")
            val task2 = TaskUiModel(taskId = 978L, taskName = "Task 978")
            val tasks = mutableListOf<UiModel>(task1, task2)
            val position = 1
            val status = TaskStatus.DONE
            val observer = mockk<Observer<TaskListViewState.Action>>()
            val slot = slot<TaskListViewState.Action>()
            val list = arrayListOf<TaskListViewState.Action>()
            viewModel.viewState.action.observeForever(observer)
            viewModel.viewState.itemsView.value = tasks
            every { observer.onChanged(capture(slot)) } answers {
                list.add(slot.captured)
            }
            prepareScenario()

            viewModel.dispatchViewAction(TaskListViewAction.OnSwipeTask(position, status))

            val expected = TaskListViewState.Action.UpdateRemovedTask(position)
            assertTrue { list.contains(expected) }
        }
    }

    @Test
    fun `should call getAlarmUseCase when dispatchViewAction is called with OnSwipeTask`() {
        testCoroutineRule.runBlockingTest {
            val tasks = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario()

            viewModel.dispatchViewAction(TaskListViewAction.OnSwipeTask(position, status))

            coVerify { getAlarmUseCase(taskId = 978L) }
        }
    }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnSwipeTask on a task with repetitive alarm`() {
        testCoroutineRule.runBlockingTest {
            val tasks = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
            val alarm = Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.WEEK,
            )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario(alarmResult = alarm)

            viewModel.dispatchViewAction(TaskListViewAction.OnSwipeTask(position, status))

            coVerify { getTaskMetricsUseCase(TaskHistoryFilter(taskId = 978L)) }
        }
    }

    private fun prepareScenario(
        tasksResult: List<UiModel> = listOf(
            TaskUiModel(
                taskName = "Task Name",
                taskId = 123L,
            )
        ),
        alarmResult: Alarm = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.WEEK,
        )
    ) {
        coEvery { getTasksViewUseCase(any()) } returns tasksResult
        coEvery { getAlarmUseCase(any()) } returns alarmResult
    }

}