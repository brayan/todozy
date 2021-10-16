package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class ScheduleAllAlarmsTest {

    private val getTasksUseCase: GetTasksUseCase = mockk(relaxed = true)
    private val getNextAlarmUseCase: GetNextAlarmUseCase = mockk(relaxed = true)
    private val scheduleAlarm: ScheduleAlarm = mockk(relaxed = true)

    private lateinit var scheduleAllAlarms: ScheduleAllAlarms

    @Before
    fun setUp() {
        scheduleAllAlarms = ScheduleAllAlarms(
            getTasksUseCase = getTasksUseCase,
            getNextAlarmUseCase = getNextAlarmUseCase,
            scheduleAlarm = scheduleAlarm,
        )
    }

    @Test
    fun `should get tasks with alarms`() = runBlocking {
        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns emptyList()

        scheduleAllAlarms()

        coVerify { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) }
        confirmVerified(getTasksUseCase)
    }

    @Test
    fun `should schedule next valid alarm when alarm is before now`() = runBlocking {
        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK)
        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)

        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)

        scheduleAllAlarms()

        coVerify { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) }
        coVerify { getNextAlarmUseCase(alarm) }
        confirmVerified(getTasksUseCase)
        confirmVerified(getNextAlarmUseCase)
    }

    @Test
    fun `should schedule alarm`() = runBlocking {
        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK)
        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)

        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)

        scheduleAllAlarms()

        coVerify { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) }
        coVerify { scheduleAlarm(alarm, 45) }
        confirmVerified(getTasksUseCase)
        confirmVerified(scheduleAlarm)
    }

    @Test
    fun `should no schedule alarm when alarm is before now`() = runBlocking {
        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.NOT_REPEAT)
        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)

        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)

        scheduleAllAlarms()

        coVerify(exactly = 0) { scheduleAlarm(alarm, 45) }
        confirmVerified(scheduleAlarm)
    }

}