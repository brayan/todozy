package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.platform.AlarmManagerService
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class ScheduleAlarmTest {

    private val alarmManagerService: AlarmManagerService = mockk(relaxed = true)

    private val scheduleAlarm = ScheduleAlarm(alarmManagerService)

    @Test
    fun `should schedule alarm on alarmManagerService`() = runBlocking {
        val taskId = 45L
        val alarm = Alarm(dateTime = Calendar.getInstance(), repeatType = RepeatType.WEEK)

        scheduleAlarm(alarm, taskId)

        coVerify { alarmManagerService.scheduleAlarm(alarm.dateTime, taskId) }
        confirmVerified(alarmManagerService)
    }

}