package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CancelAlarmScheduleTest {

    private val alarmManagerService: AlarmManagerService = mockk(relaxed = true)

    private val cancelAlarmSchedule = CancelAlarmSchedule(alarmManagerService)

    @Test
    fun `should cancel alarm from alarmManagerService`() = runBlocking {
        cancelAlarmSchedule(45)

        coVerify { alarmManagerService.cancelAlarm(45) }
        confirmVerified(alarmManagerService)
    }

}