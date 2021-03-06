package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class GetAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private lateinit var getAlarm: GetAlarm

    @Before
    fun setUp() {
        getAlarm = GetAlarm(repository)
    }

    @Test
    fun `should get alarm from repository`() = runBlocking {
        val taskId = 12L
        val alarm = Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                repeatType = RepeatType.NOT_REPEAT
        )

        coEvery { repository.getAlarmByTaskId(any()) } returns alarm

        val result = getAlarm(taskId)

        coVerify { repository.getAlarmByTaskId(taskId) }
        confirmVerified(repository)
        assertEquals(result, alarm)
    }

}