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

class ScheduleAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private lateinit var scheduleAlarm: ScheduleAlarm

    @Before
    fun setUp() {
        scheduleAlarm = ScheduleAlarm(repository)
    }

    @Test
    fun `should schedule alarm on repository`() = runBlocking {
        val taskId = 45L
        val alarm = Alarm(dateTime = Calendar.getInstance(), repeatType = RepeatType.WEEK)

        scheduleAlarm(alarm, taskId)

        coVerify { repository.scheduleAlarm(alarm, taskId) }
        confirmVerified(repository)
    }

}