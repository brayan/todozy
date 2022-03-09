package br.com.sailboat.todozy.feature.task.list.impl.data

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.feature.alarm.domain.factory.AlarmMockFactory
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.task.list.impl.data.datasource.TaskLocalDataSource
import br.com.sailboat.todozy.feature.task.list.impl.data.model.TaskData
import br.com.sailboat.todozy.feature.task.list.impl.data.repository.TaskRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TaskRepositoryImplTest {

    private val alarmRepository: AlarmRepository = mockk(relaxed = true)
    private val taskLocalDataSource: TaskLocalDataSource = mockk(relaxed = true)

    private val taskRepository = TaskRepositoryImpl(
        alarmRepository = alarmRepository,
        taskLocalDataSource = taskLocalDataSource,
    )

    @Test
    fun `should call getTask from taskLocalDataSource when getTask is called from taskRepository`() =
        runBlocking {
            val taskId = 45L
            val taskData = TaskDataMockFactory.makeTaskData()
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataResult = Result.success(taskData),
                alarmResult = alarm,
            )

            val result = taskRepository.getTask(taskId)

            val expected = Result.success(
                Task(
                    id = taskData.id,
                    name = taskData.name.orEmpty(),
                    notes = taskData.notes,
                    alarm = alarm
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getTask(taskId) }
        }

    @Test
    fun `should call getBeforeTodayTasks from taskLocalDataSource when getBeforeTodayTasks is called from taskRepository`() =
        runBlocking {
            val taskDataResult = TaskDataMockFactory.makeTaskData()
            val taskFilter = TaskFilter(category = TaskCategory.BEFORE_TODAY)
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataListResult = Result.success(listOf(taskDataResult)),
                alarmResult = alarm,
            )

            val result = taskRepository.getBeforeTodayTasks(taskFilter)

            val expected = Result.success(
                listOf(
                    Task(
                        id = taskDataResult.id,
                        name = taskDataResult.name.orEmpty(),
                        notes = taskDataResult.notes,
                        alarm = alarm
                    )
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getBeforeTodayTasks(taskFilter) }
        }

    @Test
    fun `should call getTodayTasks from taskLocalDataSource when getTodayTasks is called from taskRepository`() =
        runBlocking {
            val taskDataResult = TaskDataMockFactory.makeTaskData()
            val taskFilter = TaskFilter(category = TaskCategory.TODAY)
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataListResult = Result.success(listOf(taskDataResult)),
                alarmResult = alarm,
            )

            val result = taskRepository.getTodayTasks(taskFilter)

            val expected = Result.success(
                listOf(
                    Task(
                        id = taskDataResult.id,
                        name = taskDataResult.name.orEmpty(),
                        notes = taskDataResult.notes,
                        alarm = alarm
                    )
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getTodayTasks(taskFilter) }
        }

    @Test
    fun `should call getTomorrowTasks from taskLocalDataSource when getTomorrowTasks is called from taskRepository`() =
        runBlocking {
            val taskDataResult = TaskDataMockFactory.makeTaskData()
            val taskFilter = TaskFilter(category = TaskCategory.TOMORROW)
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataListResult = Result.success(listOf(taskDataResult)),
                alarmResult = alarm,
            )

            val result = taskRepository.getTomorrowTasks(taskFilter)

            val expected = Result.success(
                listOf(
                    Task(
                        id = taskDataResult.id,
                        name = taskDataResult.name.orEmpty(),
                        notes = taskDataResult.notes,
                        alarm = alarm
                    )
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getTomorrowTasks(taskFilter) }
        }

    @Test
    fun `should call getNextDaysTasks from taskLocalDataSource when getNextDaysTasks is called from taskRepository`() =
        runBlocking {
            val taskDataResult = TaskDataMockFactory.makeTaskData()
            val taskFilter = TaskFilter(category = TaskCategory.TOMORROW)
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataListResult = Result.success(listOf(taskDataResult)),
                alarmResult = alarm,
            )

            val result = taskRepository.getNextDaysTasks(taskFilter)

            val expected = Result.success(
                listOf(
                    Task(
                        id = taskDataResult.id,
                        name = taskDataResult.name.orEmpty(),
                        notes = taskDataResult.notes,
                        alarm = alarm
                    )
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getNextDaysTasks(taskFilter) }
        }

    @Test
    fun `should call getTasksThrowBeforeNow from taskLocalDataSource when getBeforeNowTasks is called from taskRepository`() =
        runBlocking {
            val taskDataResult = TaskDataMockFactory.makeTaskData()
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataListResult = Result.success(listOf(taskDataResult)),
                alarmResult = alarm,
            )

            val result = taskRepository.getBeforeNowTasks()

            val expected = Result.success(
                listOf(
                    Task(
                        id = taskDataResult.id,
                        name = taskDataResult.name.orEmpty(),
                        notes = taskDataResult.notes,
                        alarm = alarm
                    )
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getTasksThrowBeforeNow() }
        }

    @Test
    fun `should call getTasksWithAlarms from taskLocalDataSource when getTasksWithAlarms is called from taskRepository`() =
        runBlocking {
            val taskDataResult = TaskDataMockFactory.makeTaskData()
            val alarm = AlarmMockFactory.makeAlarm()
            prepareScenario(
                taskDataListResult = Result.success(listOf(taskDataResult)),
                alarmResult = alarm,
            )

            val result = taskRepository.getTasksWithAlarms()

            val expected = Result.success(
                listOf(
                    Task(
                        id = taskDataResult.id,
                        name = taskDataResult.name.orEmpty(),
                        notes = taskDataResult.notes,
                        alarm = alarm
                    )
                )
            )
            assertEquals(expected, result)
            coVerify { taskLocalDataSource.getTasksWithAlarms() }
        }

    @Test
    fun `should call insert from taskLocalDataSource when insert is called from taskRepository`() =
        runBlocking {
            val task = TaskMockFactory.makeTask()
            val taskId = 45L
            prepareScenario(insertResult = Result.success(taskId))

            taskRepository.insert(task)

            coVerify {
                taskLocalDataSource.insert(
                    TaskData(
                        id = taskId,
                        name = task.name,
                        notes = task.notes,
                    )
                )
            }
        }

    @Test
    fun `should call update from taskLocalDataSource when update is called from taskRepository`() =
        runBlocking {
            val task = TaskMockFactory.makeTask()
            prepareScenario()

            taskRepository.update(task)

            coVerify {
                taskLocalDataSource.update(
                    taskData = TaskData(
                        id = task.id,
                        name = task.name,
                        notes = task.notes,
                    ),
                    enabled = true,
                )
            }
        }

    private fun prepareScenario(
        taskDataResult: Result<TaskData> = Result.success(TaskDataMockFactory.makeTaskData()),
        taskDataListResult: Result<List<TaskData>> = Result.success(
            TaskDataMockFactory.makeTaskDataList()
        ),
        alarmResult: Alarm? = AlarmMockFactory.makeAlarm(),
        insertResult: Result<Long> = Result.success(45L),
        updateResult: Result<Unit?> = Result.success(Unit),
    ) {
        coEvery { taskLocalDataSource.getTask(any()) } returns taskDataResult
        coEvery { taskLocalDataSource.getBeforeTodayTasks(any()) } returns taskDataListResult
        coEvery { taskLocalDataSource.getTodayTasks(any()) } returns taskDataListResult
        coEvery { taskLocalDataSource.getTomorrowTasks(any()) } returns taskDataListResult
        coEvery { taskLocalDataSource.getNextDaysTasks(any()) } returns taskDataListResult
        coEvery { taskLocalDataSource.getTasksThrowBeforeNow() } returns taskDataListResult
        coEvery { taskLocalDataSource.getTasksWithAlarms() } returns taskDataListResult
        coEvery { taskLocalDataSource.insert(any()) } returns insertResult
        coEvery { taskLocalDataSource.update(any(), any()) } returns updateResult
        coEvery { alarmRepository.getAlarmByTaskId(any()) } returns alarmResult
    }

}