package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CancelAlarmScheduleTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private val cancelAlarmSchedule = CancelAlarmSchedule(repository)

    @Test
    fun `should cancel alarm from repository`() = runBlocking {
        cancelAlarmSchedule(45)

        coVerify { repository.cancelAlarmSchedule(45) }
        confirmVerified(repository)
    }

}