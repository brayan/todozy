package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class DeleteAlarmUseCaseImplTest {

    private val repository: AlarmRepository = mockk(relaxed = true)
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase = mockk(relaxed = true)

    private val deleteAlarmUseCase = DeleteAlarmUseCaseImpl(
        alarmRepository = repository,
        cancelAlarmScheduleUseCase = cancelAlarmScheduleUseCase,
    )

    @Test
    fun `should delete alarm from repository`() = runBlocking {
        deleteAlarmUseCase(45)

        coVerify { repository.deleteAlarmByTask(45) }
        confirmVerified(repository)
    }

    @Test
    fun `should cancel alarm from cancelAlarmSchedule`() = runBlocking {
        deleteAlarmUseCase(45)

        coVerify { cancelAlarmScheduleUseCase(45) }
        confirmVerified(cancelAlarmScheduleUseCase)
    }
}
