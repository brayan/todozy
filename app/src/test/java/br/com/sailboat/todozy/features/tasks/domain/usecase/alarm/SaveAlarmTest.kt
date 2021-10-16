package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class SaveAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase = mockk(relaxed = true)
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase = mockk(relaxed = true)

    private val saveAlarm = SaveAlarm(
        alarmRepository = repository,
        cancelAlarmScheduleUseCase = cancelAlarmScheduleUseCase,
        scheduleAlarmUseCase = scheduleAlarmUseCase,
    )

    private val taskId = 45L
    private val alarm = Alarm(dateTime = Calendar.getInstance(), repeatType = RepeatType.WEEK)

    @Test
    fun `should save alarm on repository`() = runBlocking {
        saveAlarm(alarm, taskId)

        coVerify { repository.save(alarm, taskId) }
        confirmVerified(repository)
    }

    @Test
    fun `should cancel current alarm schedule when saving`() = runBlocking {
        saveAlarm(alarm, taskId)

        coVerify { cancelAlarmScheduleUseCase(taskId) }
        confirmVerified(cancelAlarmScheduleUseCase)
    }

    @Test
    fun `should schedule alarm when saving`() = runBlocking {
        saveAlarm(alarm, taskId)

        coVerify { scheduleAlarmUseCase(alarm, taskId) }
        confirmVerified(scheduleAlarmUseCase)
    }

}