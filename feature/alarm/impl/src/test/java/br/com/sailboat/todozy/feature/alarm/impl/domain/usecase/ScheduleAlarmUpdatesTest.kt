package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar

class ScheduleAlarmUpdatesTest {

    private val alarmManagerService: AlarmManagerService = mockk(relaxed = true)

    private val scheduleAlarmUpdates = ScheduleAlarmUpdates(alarmManagerService)

    @Test
    fun `should schedule alarm updates to the first hour of tomorrow on alarmManagerService`() =
        runBlocking {
            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            scheduleAlarmUpdates()

            coVerify { alarmManagerService.scheduleAlarmUpdates(calendar) }
            confirmVerified(alarmManagerService)
        }
}
