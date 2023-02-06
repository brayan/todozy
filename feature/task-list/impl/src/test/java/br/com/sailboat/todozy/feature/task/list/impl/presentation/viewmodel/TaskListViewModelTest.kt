package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.impl.presentation.factory.TaskListUiModelFactory
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.uicomponent.impl.helper.CoroutinesTestRule
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.model.UiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class TaskListViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getTasksUseCase: GetTasksUseCase = mockk(relaxed = true)
    private val getAlarmUseCase: GetAlarmUseCase = mockk(relaxed = true)
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase = mockk(relaxed = true)
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val completeTaskUseCase: CompleteTaskUseCase = mockk(relaxed = true)
    private val taskListUiModelFactory: TaskListUiModelFactory = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)

    private val viewModel = TaskListViewModel(
        getTasksUseCase = getTasksUseCase,
        getAlarmUseCase = getAlarmUseCase,
        scheduleAllAlarmsUseCase = scheduleAllAlarmsUseCase,
        getTaskMetricsUseCase = getTaskMetricsUseCase,
        completeTaskUseCase = completeTaskUseCase,
        taskListUiModelFactory = taskListUiModelFactory,
        logService = logService,
    )

    @Test
    fun `should send CloseNotifications when dispatchViewAction is called with OnStart`() {
        runBlocking {
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)

            assertEquals(
                TaskListViewAction.CloseNotifications,
                viewModel.viewState.action.value
            )
        }
    }

    @Test
    fun `should call getTasksUseCase when dispatchViewAction is called with OnStart`() {
        runBlocking {
            val tasksView =
                mutableListOf<UiModel>(TaskUiModel(taskId = 543L, taskName = "Task 543"))
            prepareScenario(tasksView = tasksView)

            viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)

            coVerifyOrder {
                getTasksUseCase(TaskFilter(category = TaskCategory.BEFORE_TODAY))
                getTasksUseCase(TaskFilter(category = TaskCategory.TODAY))
                getTasksUseCase(TaskFilter(category = TaskCategory.TOMORROW))
                getTasksUseCase(TaskFilter(category = TaskCategory.NEXT_DAYS))
            }
            val expected = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
            )
            assertEquals(expected, viewModel.viewState.itemsView.value)
        }
    }

    @Test
    fun `should call scheduleAllAlarmsUseCase when dispatchViewAction is called with OnStart`() {
        runBlocking {
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)

            coVerify(exactly = 1) { scheduleAllAlarmsUseCase() }
        }
    }

    @Test
    fun `should navigate to about screen when dispatchViewAction is called with OnClickMenuAbout`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuAbout)

        assertEquals(TaskListViewAction.NavigateToAbout, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to history screen when dispatchViewAction is called with OnClickMenuHistory`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuHistory)

        assertEquals(TaskListViewAction.NavigateToHistory, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to settings screen when dispatchViewAction is called with OnClickMenuSettings`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuSettings)

        assertEquals(TaskListViewAction.NavigateToSettings, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task form screen when dispatchViewAction is called with OnClickNewTask`() {
        prepareScenario()
        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickNewTask)

        assertEquals(TaskListViewAction.NavigateToTaskForm, viewModel.viewState.action.value)
    }

    @Test
    fun `should navigate to task details screen when dispatchViewAction is called with OnClickTask`() {
        prepareScenario()
        val taskId = 123L

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickTask(taskId = taskId))

        val expected = TaskListViewAction.NavigateToTaskDetails(taskId = taskId)
        assertEquals(expected, viewModel.viewState.action.value)
    }

    @Test
    fun `should call getTasksUseCase and search for tasks when dispatchViewAction is called with OnInputSearchTerm`() {
        runBlocking {
            val term = "Term"
            val tasksView = mutableListOf<UiModel>(
                TaskUiModel(
                    taskId = 543L,
                    taskName = "Task 543",
                )
            )
            prepareScenario(tasksView = tasksView)

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSubmitSearchTerm(term = term))

            coVerifyOrder {
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.BEFORE_TODAY))
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.TODAY))
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.TOMORROW))
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.NEXT_DAYS))
            }
            val expected = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
            )
            assertEquals(expected, viewModel.viewState.itemsView.value)
        }
    }

    @Test
    fun `should call completeTaskUseCase when dispatchViewAction is called with OnSwipeTask`() {
        runTest {
            val tasks = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))

            coVerify(exactly = 1) { completeTaskUseCase(taskId = 978L, status = status) }
        }
    }

    @Test
    fun `should update removed task when dispatchViewAction is called with OnSwipeTask`() {
        runTest {
            val task1 = TaskUiModel(taskId = 543L, taskName = "Task 543")
            val task2 = TaskUiModel(taskId = 978L, taskName = "Task 978")
            val tasks = mutableListOf<UiModel>(task1, task2)
            val position = 1
            val status = TaskStatus.DONE
            val observer = mockk<Observer<TaskListViewAction>>()
            val slot = slot<TaskListViewAction>()
            val list = arrayListOf<TaskListViewAction>()
            viewModel.viewState.action.observeForever(observer)
            viewModel.viewState.itemsView.value = tasks
            every { observer.onChanged(capture(slot)) } answers {
                list.add(slot.captured)
            }
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))

            val expected = TaskListViewAction.UpdateRemovedTask(position)
            assertTrue { list.contains(expected) }
        }
    }

    @Test
    fun `should call getAlarmUseCase when dispatchViewAction is called with OnSwipeTask`() =
        runTest {
            val tasks = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))

            coVerify { getAlarmUseCase(taskId = 978L) }
        }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnSwipeTask on a task with repetitive alarm`() {
        runTest {
            val tasks = mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
            val alarmResult = Result.success(
                Alarm(
                    dateTime = Calendar.getInstance(),
                    repeatType = RepeatType.WEEK,
                )
            )

            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario(alarmResult = alarmResult)

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))

            coVerify { getTaskMetricsUseCase(TaskHistoryFilter(taskId = 978L)) }
        }
    }

    private fun prepareScenario(
        tasksView: List<UiModel> = listOf(
            TaskUiModel(
                taskName = "Task Name",
                taskId = 42L,
            )
        ),
        tasksResult: Result<List<Task>> = Result.success(
            listOf(
                Task(
                    id = 42L,
                    name = "Task Name",
                    notes = null,
                )
            )
        ),
        alarmResult: Result<Alarm> = Result.success(
            Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.WEEK,
            )
        )
    ) {
        coEvery { getTasksUseCase(any()) } returns tasksResult
        coEvery { getAlarmUseCase(any()) } returns alarmResult
        coEvery { taskListUiModelFactory.create(any(), any()) } returns tasksView
    }
}
