package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarm
import br.com.sailboat.todozy.feature.alarm.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.domain.model.RepeatType
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