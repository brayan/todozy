package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter
import br.com.sailboat.todozy.feature.task.history.domain.usecase.GetTaskProgressUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.feature.task.list.impl.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.feature.task.list.impl.presentation.factory.TaskListUiModelFactory
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.model.Entity
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
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val TASK_SWIPE_DELAY_IN_MILLIS = 4000L

@ExperimentalCoroutinesApi
internal class TaskListViewModelTest {
    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getTasksUseCase: GetTasksUseCase = mockk(relaxed = true)
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase = mockk(relaxed = true)
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk(relaxed = true)
    private val getTaskProgressUseCase: GetTaskProgressUseCase = mockk(relaxed = true)
    private val completeTaskUseCase: CompleteTaskUseCase = mockk(relaxed = true)
    private val taskListUiModelFactory: TaskListUiModelFactory = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)

    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setup() {
        viewModel =
            TaskListViewModel(
                getTasksUseCase = getTasksUseCase,
                scheduleAllAlarmsUseCase = scheduleAllAlarmsUseCase,
                getTaskMetricsUseCase = getTaskMetricsUseCase,
                getTaskProgressUseCase = getTaskProgressUseCase,
                completeTaskUseCase = completeTaskUseCase,
                taskListUiModelFactory = taskListUiModelFactory,
                logService = logService,
                dispatcherProvider = coroutinesTestRule.dispatcherProvider,
            )
    }

    @Test
    fun `should send CloseNotifications when dispatchViewAction is called with OnStart`() = runTest(coroutinesTestRule.dispatcher) {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        assertEquals(
            TaskListViewAction.CloseNotifications,
            viewModel.viewState.viewAction.value,
        )
    }

    @Test
    fun `should call getTasksUseCase when dispatchViewAction is called with OnStart`() = runTest(coroutinesTestRule.dispatcher) {
        val tasksView =
            mutableListOf<UiModel>(TaskUiModel(taskId = 543L, taskName = "Task 543"))
        prepareScenario(tasksView = tasksView)

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        coVerifyOrder {
            getTasksUseCase(TaskFilter(category = TaskCategory.BEFORE_TODAY))
            getTasksUseCase(TaskFilter(category = TaskCategory.TODAY))
            getTasksUseCase(TaskFilter(category = TaskCategory.TOMORROW))
            getTasksUseCase(TaskFilter(category = TaskCategory.NEXT_DAYS))
        }
        val expected =
            mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
            )
        assertEquals(expected, viewModel.viewState.itemsView.value)
    }

    @Test
    fun `should call scheduleAllAlarmsUseCase when dispatchViewAction is called with OnStart`() = runTest(coroutinesTestRule.dispatcher) {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        coVerify(exactly = 1) { scheduleAllAlarmsUseCase() }
        coVerify { getTaskProgressUseCase(TaskProgressFilter(TaskProgressRange.LAST_YEAR)) }
    }

    @Test
    fun `should hide metrics and progress when no tasks are loaded`() = runTest(coroutinesTestRule.dispatcher) {
        prepareScenario(tasksView = emptyList(), tasksResult = Result.success(emptyList()))

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        assertEquals(null, viewModel.viewState.taskMetrics.value)
        assertTrue(viewModel.viewState.taskProgressDays.value?.isEmpty() == true)
        assertEquals(false, viewModel.viewState.taskProgressLoading.value)
        coVerify(exactly = 0) { getTaskMetricsUseCase(any()) }
        coVerify(exactly = 0) { getTaskProgressUseCase(any()) }
    }

    @Test
    fun `should navigate to about screen when dispatchViewAction is called with OnClickMenuAbout`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuAbout)

        assertEquals(TaskListViewAction.NavigateToAbout, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should navigate to history screen when dispatchViewAction is called with OnClickMenuHistory`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuHistory)

        assertEquals(TaskListViewAction.NavigateToHistory, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should navigate to settings screen when dispatchViewAction is called with OnClickMenuSettings`() {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickMenuSettings)

        assertEquals(TaskListViewAction.NavigateToSettings, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should navigate to task form screen when dispatchViewAction is called with OnClickNewTask`() {
        prepareScenario()
        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickNewTask)

        assertEquals(TaskListViewAction.NavigateToTaskForm, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should navigate to task details screen when dispatchViewAction is called with OnClickTask`() {
        prepareScenario()
        val taskId = 123L

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickTask(taskId = taskId))

        val expected = TaskListViewAction.NavigateToTaskDetails(taskId = taskId)
        assertEquals(expected, viewModel.viewState.viewAction.value)
    }

    @Test
    fun `should call getTasksUseCase and search for tasks when dispatchViewAction is called with OnInputSearchTerm`() =
        runTest(coroutinesTestRule.dispatcher) {
            val term = "Term"
            val tasksView =
                mutableListOf<UiModel>(
                    TaskUiModel(
                        taskId = 543L,
                        taskName = "Task 543",
                    ),
                )
            prepareScenario(tasksView = tasksView)

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSubmitSearchTerm(term = term))
            advanceUntilIdle()

            coVerifyOrder {
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.BEFORE_TODAY))
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.TODAY))
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.TOMORROW))
                getTasksUseCase(TaskFilter(text = term, category = TaskCategory.NEXT_DAYS))
            }
            val expected =
                mutableListOf<UiModel>(
                    TaskUiModel(taskId = 543L, taskName = "Task 543"),
                    TaskUiModel(taskId = 543L, taskName = "Task 543"),
                    TaskUiModel(taskId = 543L, taskName = "Task 543"),
                    TaskUiModel(taskId = 543L, taskName = "Task 543"),
                )
            assertEquals(expected, viewModel.viewState.itemsView.value)
        }

    @Test
    fun `should call completeTaskUseCase when dispatchViewAction is called with OnSwipeTask`() {
        runTest(coroutinesTestRule.dispatcher) {
            val tasks =
                mutableListOf<UiModel>(
                    TaskUiModel(taskId = 543L, taskName = "Task 543"),
                    TaskUiModel(taskId = 978L, taskName = "Task 978"),
                )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))
            advanceTimeBy(TASK_SWIPE_DELAY_IN_MILLIS)
            advanceUntilIdle()

            coVerify(exactly = 1) { completeTaskUseCase(taskId = 978L, status = status) }
        }
    }

    @Test
    fun `should update removed task when dispatchViewAction is called with OnSwipeTask`() {
        runTest(coroutinesTestRule.dispatcher) {
            val task1 = TaskUiModel(taskId = 543L, taskName = "Task 543")
            val task2 = TaskUiModel(taskId = 978L, taskName = "Task 978")
            val tasks = mutableListOf<UiModel>(task1, task2)
            val position = 1
            val status = TaskStatus.DONE
            val observer = mockk<Observer<TaskListViewAction>>()
            val slot = slot<TaskListViewAction>()
            val list = arrayListOf<TaskListViewAction>()
            viewModel.viewState.viewAction.observeForever(observer)
            viewModel.viewState.itemsView.value = tasks
            every { observer.onChanged(capture(slot)) } answers {
                list.add(slot.captured)
            }
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))
            advanceTimeBy(TASK_SWIPE_DELAY_IN_MILLIS)
            advanceUntilIdle()

            val expected = TaskListViewAction.UpdateRemovedTask(position)
            assertTrue { list.contains(expected) }
            assertTrue {
                viewModel.viewState.itemsView.value
                    ?.filterIsInstance<TaskUiModel>()
                    ?.none { it.taskId == task2.taskId } == true
            }
        }
    }

    @Test
    fun `should refresh metrics when dispatchViewAction is called with OnSwipeTask`() = runTest(coroutinesTestRule.dispatcher) {
        val tasks =
            mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = 978L, taskName = "Task 978"),
            )
        val position = 1
        val status = TaskStatus.DONE
        viewModel.viewState.itemsView.value = tasks
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))
        advanceTimeBy(TASK_SWIPE_DELAY_IN_MILLIS)
        advanceUntilIdle()

        coVerify {
            getTaskMetricsUseCase(
                match { filter ->
                    filter.taskId == Entity.NO_ID &&
                        filter.initialDate != null &&
                        filter.finalDate != null
                },
            )
        }
    }

    @Test
    fun `should show inline metrics when dispatchViewAction is called with OnSwipeTask`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 978L
        val tasks =
            mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
                TaskUiModel(taskId = taskId, taskName = "Task 978"),
            )
        val inlineMetrics = TaskMetrics(doneTasks = 2, notDoneTasks = 1, consecutiveDone = 3)
        prepareScenario(
            tasksView = tasks,
            tasksResult =
                Result.success(
                    listOf(
                        Task(id = 543L, name = "Task 543", notes = null),
                        Task(id = taskId, name = "Task 978", notes = null),
                    ),
                ),
        )
        coEvery {
            getTaskMetricsUseCase(match { it.taskId == taskId })
        } returns Result.success(inlineMetrics)

        viewModel.viewState.itemsView.value = tasks

        viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(1, TaskStatus.DONE))
        runCurrent()

        val updatedTask = viewModel.viewState.itemsView.value?.get(1) as TaskUiModel
        assertTrue(updatedTask.showInlineMetrics)
        assertEquals(TaskMetrics(doneTasks = 3, notDoneTasks = 1, consecutiveDone = 4), updatedTask.inlineMetrics)
        assertEquals(TaskStatus.DONE, updatedTask.inlineStatus)
    }

    @Test
    fun `should call getTaskMetricsUseCase when dispatchViewAction is called with OnSwipeTask on a task with repetitive alarm`() {
        runTest(coroutinesTestRule.dispatcher) {
            val tasks =
                mutableListOf<UiModel>(
                    TaskUiModel(taskId = 543L, taskName = "Task 543"),
                    TaskUiModel(taskId = 978L, taskName = "Task 978"),
                )
            val position = 1
            val status = TaskStatus.DONE
            viewModel.viewState.itemsView.value = tasks
            prepareScenario()

            viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position, status))
            advanceUntilIdle()

            coVerify {
                getTaskMetricsUseCase(
                    match { filter ->
                        filter.taskId == 978L &&
                            filter.initialDate != null &&
                            filter.finalDate != null
                    },
                )
            }
        }
    }

    @Test
    fun `should show optimistic metrics while commit is pending`() = runTest(coroutinesTestRule.dispatcher) {
        val tasks =
            mutableListOf<UiModel>(
                TaskUiModel(taskId = 543L, taskName = "Task 543"),
            )
        prepareScenario(tasksView = tasks)

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(position = 0, status = TaskStatus.DONE))
        runCurrent()

        assertEquals(TaskMetrics(doneTasks = 1, notDoneTasks = 0, consecutiveDone = 1), viewModel.viewState.taskMetrics.value)
    }

    @Test
    fun `should undo swipe and restore task`() = runTest(coroutinesTestRule.dispatcher) {
        val taskId = 978L
        val tasks =
            mutableListOf<UiModel>(
                TaskUiModel(taskId = taskId, taskName = "Task 978"),
            )
        prepareScenario(
            tasksView = tasks,
            tasksResult =
                Result.success(
                    listOf(
                        Task(id = taskId, name = "Task 978", notes = null),
                    ),
                ),
        )

        viewModel.viewState.itemsView.value = tasks

        viewModel.dispatchViewIntent(TaskListViewIntent.OnSwipeTask(0, TaskStatus.DONE))
        runCurrent()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnClickUndoTask(taskId, TaskStatus.DONE))
        advanceTimeBy(TASK_SWIPE_DELAY_IN_MILLIS)
        advanceUntilIdle()

        coVerify(exactly = 0) { completeTaskUseCase(taskId, any()) }
        val restoredTask =
            viewModel.viewState.itemsView.value?.filterIsInstance<TaskUiModel>()?.firstOrNull()
        assertTrue(restoredTask?.showInlineMetrics == false)
    }

    @Test
    fun `should reload progress when progress range changes`() = runTest(coroutinesTestRule.dispatcher) {
        prepareScenario()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        viewModel.dispatchViewIntent(TaskListViewIntent.OnSelectProgressRange(TaskProgressRange.LAST_30_DAYS))
        advanceUntilIdle()

        coVerify {
            getTaskProgressUseCase(TaskProgressFilter(range = TaskProgressRange.LAST_30_DAYS))
        }
        assertEquals(TaskProgressRange.LAST_30_DAYS, viewModel.viewState.taskProgressRange.value)
    }

    @Test
    fun `should sum consecutive done across tasks when loading metrics`() = runTest(coroutinesTestRule.dispatcher) {
        val taskB =
            Task(
                id = 101L,
                name = "Task B",
                notes = null,
            )
        val taskC =
            Task(
                id = 202L,
                name = "Task C",
                notes = null,
            )
        val taskD =
            Task(
                id = 303L,
                name = "Task D",
                notes = null,
            )

        coEvery { getTasksUseCase(match { it.category == TaskCategory.TODAY }) } returns
            Result.success(listOf(taskB, taskC, taskD))
        coEvery { getTasksUseCase(match { it.category != TaskCategory.TODAY }) } returns Result.success(emptyList())
        coEvery { taskListUiModelFactory.create(any(), any()) } returns emptyList()
        coEvery { getTaskProgressUseCase(any()) } returns Result.success(emptyList())
        coEvery { getTaskMetricsUseCase(match { it.taskId == Entity.NO_ID }) } returns
            Result.success(TaskMetrics(doneTasks = 70, notDoneTasks = 5, consecutiveDone = 0))
        coEvery { getTaskMetricsUseCase(match { it.taskId == taskB.id }) } returns
            Result.success(TaskMetrics(doneTasks = 10, notDoneTasks = 0, consecutiveDone = 10))
        coEvery { getTaskMetricsUseCase(match { it.taskId == taskC.id }) } returns
            Result.success(TaskMetrics(doneTasks = 20, notDoneTasks = 0, consecutiveDone = 20))
        coEvery { getTaskMetricsUseCase(match { it.taskId == taskD.id }) } returns
            Result.success(TaskMetrics(doneTasks = 40, notDoneTasks = 0, consecutiveDone = 40))

        viewModel.dispatchViewIntent(TaskListViewIntent.OnStart)
        advanceUntilIdle()

        assertEquals(
            TaskMetrics(doneTasks = 70, notDoneTasks = 5, consecutiveDone = 70),
            viewModel.viewState.taskMetrics.value,
        )
    }

    private fun prepareScenario(
        tasksView: List<UiModel> =
            listOf(
                TaskUiModel(
                    taskName = "Task Name",
                    taskId = 42L,
                ),
            ),
        tasksResult: Result<List<Task>> =
            Result.success(
                listOf(
                    Task(
                        id = 42L,
                        name = "Task Name",
                        notes = null,
                    ),
                ),
            ),
    ) {
        coEvery { getTasksUseCase(any()) } returns tasksResult
        coEvery { taskListUiModelFactory.create(any(), any()) } returns tasksView
        coEvery { getTaskProgressUseCase(any()) } returns Result.success(emptyList())
        coEvery { getTaskMetricsUseCase(any()) } returns Result.success(TaskMetrics(0, 0, 0))
        coEvery { completeTaskUseCase(any(), any()) } returns Result.success(Unit)
    }
}
