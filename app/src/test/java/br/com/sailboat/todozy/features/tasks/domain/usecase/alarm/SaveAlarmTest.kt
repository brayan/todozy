package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class SaveAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)
    private val cancelAlarmSchedule: CancelAlarmSchedule = mockk(relaxed = true)
    private val scheduleAlarm: ScheduleAlarm = mockk(relaxed = true)

    private lateinit var saveAlarm: SaveAlarm

    private val taskId = 45L
    private val alarm = Alarm(dateTime = Calendar.getInstance(), repeatType = RepeatType.WEEK)

    @Before
    fun setUp() {
        saveAlarm = SaveAlarm(repository, cancelAlarmSchedule, scheduleAlarm)
    }

    @Test
    fun `should save alarm on repository`() = runBlocking {
        saveAlarm(alarm, taskId)

        coVerify { repository.save(alarm, taskId) }
        confirmVerified(repository)
    }

    @Test
    fun `should cancel current alarm schedule when saving`() = runBlocking {
        saveAlarm(alarm, taskId)

        coVerify { cancelAlarmSchedule(taskId) }
        confirmVerified(cancelAlarmSchedule)
    }

    @Test
    fun `should schedule alarm when saving`() = runBlocking {
        saveAlarm(alarm, taskId)

        coVerify { scheduleAlarm(alarm, taskId) }
        confirmVerified(scheduleAlarm)
    }

}