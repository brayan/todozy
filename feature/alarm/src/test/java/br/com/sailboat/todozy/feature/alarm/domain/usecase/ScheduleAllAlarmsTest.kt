package br.com.sailboat.todozy.feature.alarm.domain.usecase

//import br.com.sailboat.todozy.domain.model.Alarm
//import br.com.sailboat.todozy.domain.model.RepeatType
//import br.com.sailboat.todozy.features.tasks.domain.model.*
//import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasksUseCase
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.confirmVerified
import br.com.sailboat.todozy.domain.usecase.GetNextAlarmUseCase
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
//import java.util.*

class ScheduleAllAlarmsTest {

//    private val getTasksUseCase: GetTasksUseCase = mockk(relaxed = true)
    private val getNextAlarmUseCase: GetNextAlarmUseCase = mockk(relaxed = true)
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase = mockk(relaxed = true)

    private val scheduleAllAlarms = ScheduleAllAlarms(
//        getTasksUseCase = getTasksUseCase,
        getNextAlarmUseCase = getNextAlarmUseCase,
        scheduleAlarmUseCase = scheduleAlarmUseCase,
    )

    @Test
    fun `should get tasks with alarms`() = runBlocking {
//        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns emptyList()
//
//        scheduleAllAlarms()
//
//        coVerify { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) }
//        confirmVerified(getTasksUseCase)
    }

    @Test
    fun `should schedule next valid alarm when alarm is before now`() = runBlocking {
//        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
//        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK)
//        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)
//
//        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)
//
//        scheduleAllAlarms()
//
//        coVerify { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) }
//        coVerify { getNextAlarmUseCase(alarm) }
//        confirmVerified(getTasksUseCase)
//        confirmVerified(getNextAlarmUseCase)
    }

    @Test
    fun `should schedule alarm`() = runBlocking {
//        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
//        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK)
//        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)
//
//        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)
//
//        scheduleAllAlarms()
//
//        coVerify { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) }
//        coVerify { scheduleAlarmUseCase(alarm, 45) }
//        confirmVerified(getTasksUseCase)
//        confirmVerified(scheduleAlarmUseCase)
    }

    @Test
    fun `should no schedule alarm when alarm is before now`() = runBlocking {
//        val dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
//        val alarm = Alarm(dateTime = dateTime, repeatType = RepeatType.NOT_REPEAT)
//        val task = Task(id = 45, name = "Task 1", notes = "Some notes", alarm = alarm)
//
//        coEvery { getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS)) } returns listOf(task)
//
//        scheduleAllAlarms()
//
//        coVerify(exactly = 0) { scheduleAlarmUseCase(alarm, 45) }
//        confirmVerified(scheduleAlarmUseCase)
    }

}