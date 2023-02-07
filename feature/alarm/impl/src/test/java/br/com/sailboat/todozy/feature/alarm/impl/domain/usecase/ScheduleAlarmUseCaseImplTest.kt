package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar

internal class ScheduleAlarmUseCaseImplTest {

    private val alarmManagerService: AlarmManagerService = mockk(relaxed = true)

    private val scheduleAlarmUseCase = ScheduleAlarmUseCaseImpl(alarmManagerService)

    @Test
    fun `should schedule alarm on alarmManagerService`() = runBlocking {
        val taskId = 45L
        val alarm = Alarm(dateTime = Calendar.getInstance(), repeatType = RepeatType.WEEK)

        scheduleAlarmUseCase(alarm, taskId)

        coVerify { alarmManagerService.scheduleAlarm(alarm.dateTime, taskId) }
        confirmVerified(alarmManagerService)
    }
}
