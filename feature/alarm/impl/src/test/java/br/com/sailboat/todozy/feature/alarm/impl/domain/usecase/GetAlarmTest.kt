package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class GetAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private val getAlarm = GetAlarm(repository)

    @Test
    fun `should get alarm from repository`() = runBlocking {
        val taskId = 12L
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val alarmResult = Result.success(alarm)

        coEvery { repository.getAlarmByTaskId(any()) } returns alarmResult

        val result = getAlarm(taskId)

        coVerify { repository.getAlarmByTaskId(taskId) }
        confirmVerified(repository)
        assertEquals(result, alarmResult)
    }
}
