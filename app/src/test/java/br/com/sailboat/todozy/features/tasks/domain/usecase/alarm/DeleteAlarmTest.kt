package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)
    private val cancelAlarmSchedule: CancelAlarmSchedule = mockk(relaxed = true)

    private lateinit var deleteAlarm: DeleteAlarm

    @Before
    fun setUp() {
        deleteAlarm = DeleteAlarm(repository, cancelAlarmSchedule)
    }

    @Test
    fun `should delete alarm from repository`() = runBlocking {
        deleteAlarm(45)

        coVerify { repository.deleteAlarmByTask(45) }
        confirmVerified(repository)
    }

    @Test
    fun `should cancel alarm from cancelAlarmSchedule`() = runBlocking {
        deleteAlarm(45)

        coVerify { cancelAlarmSchedule(45) }
        confirmVerified(cancelAlarmSchedule)
    }

}