package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase = mockk(relaxed = true)

    private val deleteAlarm = DeleteAlarm(
        alarmRepository = repository,
        cancelAlarmScheduleUseCase = cancelAlarmScheduleUseCase,
    )

    @Test
    fun `should delete alarm from repository`() = runBlocking {
        deleteAlarm(45)

        coVerify { repository.deleteAlarmByTask(45) }
        confirmVerified(repository)
    }

    @Test
    fun `should cancel alarm from cancelAlarmSchedule`() = runBlocking {
        deleteAlarm(45)

        coVerify { cancelAlarmScheduleUseCase(45) }
        confirmVerified(cancelAlarmScheduleUseCase)
    }

}