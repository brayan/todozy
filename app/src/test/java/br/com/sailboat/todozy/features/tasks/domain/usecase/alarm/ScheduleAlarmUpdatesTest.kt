package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class ScheduleAlarmUpdatesTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private val scheduleAlarmUpdates = ScheduleAlarmUpdates(repository)
    @Test
    fun `should schedule alarm updates to the first hour of tomorrow on repository`() = runBlocking {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        scheduleAlarmUpdates()

        coVerify { repository.scheduleAlarmUpdates(calendar) }
        confirmVerified(repository)
    }
}