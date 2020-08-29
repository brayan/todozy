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

class GetNextAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private lateinit var getNextAlarm: GetNextAlarm

    @Before
    fun setUp() {
        getNextAlarm = GetNextAlarm(repository)
    }

    @Test
    fun `should get next valid alarm from repository`() = runBlocking {
        val alarm = Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                repeatType = RepeatType.NOT_REPEAT
        )

        coEvery { repository.getNextValidAlarm(any()) } returns alarm

        val result = getNextAlarm(alarm)
        coVerify { repository.getNextValidAlarm(any()) }
        confirmVerified(repository)
        assertEquals(result, alarm)
    }
}