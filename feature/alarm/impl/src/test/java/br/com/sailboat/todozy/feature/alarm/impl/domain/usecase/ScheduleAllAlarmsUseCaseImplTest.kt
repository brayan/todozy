package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar

internal class ScheduleAllAlarmsUseCaseImplTest {
    private val getTasksUseCase: GetTasksUseCase = mockk(relaxed = true)
    private val getNextAlarmUseCase: GetNextAlarmUseCase = mockk(relaxed = true)
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase = mockk(relaxed = true)

    private val scheduleAllAlarmsUseCase =
        ScheduleAllAlarmsUseCaseImpl(
            getTasksUseCase = getTasksUseCase,
            getNextAlarmUseCase = getNextAlarmUseCase,
            scheduleAlarmUseCase = scheduleAlarmUseCase,
        )

    @Test
    fun `should get tasks with alarms`() = runBlocking {
        prepareScenario()

        scheduleAllAlarmsUseCase()

        coVerify { getTasksUseCase(TaskFilter(category = TaskCategory.WITH_ALARMS)) }
        confirmVerified(getTasksUseCase)
    }

    @Test
    fun `should schedule next valid alarm when alarm is before now`() = runBlocking {
        val alarm =
            Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                repeatType = RepeatType.WEEK,
            )
        val task =
            Task(
                id = 42L,
                name = "Task 1",
                notes = "Some notes",
                alarm = alarm,
            )
        prepareScenario(tasksResult = Result.success(listOf(task)))

        scheduleAllAlarmsUseCase()

        coVerify { getTasksUseCase(TaskFilter(category = TaskCategory.WITH_ALARMS)) }
        coVerify { getNextAlarmUseCase(alarm) }
        confirmVerified(getTasksUseCase)
        confirmVerified(getNextAlarmUseCase)
    }

    @Test
    fun `should schedule alarm`() = runBlocking {
        val alarm =
            Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) },
                repeatType = RepeatType.WEEK,
            )
        val task =
            Task(
                id = 42L,
                name = "Task 1",
                notes = "Some notes",
                alarm = alarm,
            )
        prepareScenario(tasksResult = Result.success(listOf(task)))

        scheduleAllAlarmsUseCase()

        coVerify { getTasksUseCase(TaskFilter(category = TaskCategory.WITH_ALARMS)) }
        coVerify { scheduleAlarmUseCase(alarm, 42L) }
        confirmVerified(getTasksUseCase)
        confirmVerified(scheduleAlarmUseCase)
    }

    @Test
    fun `should not schedule alarm when alarm is before now`() = runBlocking {
        val alarm =
            Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                repeatType = RepeatType.NOT_REPEAT,
            )
        val task =
            Task(
                id = 42L,
                name = "Task 1",
                notes = "Some notes",
                alarm = alarm,
            )
        prepareScenario(tasksResult = Result.success(listOf(task)))

        scheduleAllAlarmsUseCase()

        coVerify(exactly = 0) { scheduleAlarmUseCase(alarm, 42L) }
        confirmVerified(scheduleAlarmUseCase)
    }

    private fun prepareScenario(
        tasksResult: Result<List<Task>> =
            Result.success(
                listOf(
                    Task(
                        id = 42L,
                        name = "Task Name",
                        notes = "Some notes",
                    ),
                ),
            ),
    ) {
        coEvery { getTasksUseCase(any()) } returns tasksResult
    }
}
