package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.GetTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class ScheduleAllAlarmsTest {

    private val getTasks: GetTasks = mockk(relaxed = true)
    private val getNextAlarm: GetNextAlarm = mockk(relaxed = true)
    private val scheduleAlarm: ScheduleAlarm = mockk(relaxed = true)

    private lateinit var scheduleAllAlarms: ScheduleAllAlarms

    @Before
    fun setUp() {
        scheduleAllAlarms = ScheduleAllAlarms(getTasks, getNextAlarm, scheduleAlarm)
    }

    @Test
    fun `should get tasks with alarms`() = runBlocking {
        coEvery { getTasks(TaskFilter(TaskCategory.WITH_ALARMS)) } returns emptyList()

        scheduleAllAlarms()

        coVerify { getTasks(TaskFilter(TaskCategory.WITH_ALARMS)) }
        confirmVerified(getTasks)
    }

    @Test
    fun `should schedule next valid alarm when alarm is before now`() = runBlocking {
        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK)
        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)

        coEvery { getTasks(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)

        scheduleAllAlarms()

        coVerify { getTasks(TaskFilter(TaskCategory.WITH_ALARMS)) }
        coVerify { getNextAlarm(alarm) }
        confirmVerified(getTasks)
        confirmVerified(getNextAlarm)
    }

    @Test
    fun `should schedule alarm`() = runBlocking {
        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK)
        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)

        coEvery { getTasks(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)

        scheduleAllAlarms()

        coVerify { getTasks(TaskFilter(TaskCategory.WITH_ALARMS)) }
        coVerify { scheduleAlarm(alarm, 45) }
        confirmVerified(getTasks)
        confirmVerified(scheduleAlarm)
    }

}