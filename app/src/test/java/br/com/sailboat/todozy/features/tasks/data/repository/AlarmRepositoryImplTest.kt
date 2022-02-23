package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.features.tasks.data.datasource.local.AlarmLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.factory.AlarmDataMockFactory.makeAlarmData
import br.com.sailboat.todozy.features.tasks.data.model.AlarmData
import br.com.sailboat.todozy.features.tasks.domain.factory.AlarmMockFactory.makeAlarm
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class AlarmRepositoryImplTest {

    private val alarmLocalDataSource: AlarmLocalDataSource = mockk(relaxed = true)

    private val alarmRepository = AlarmRepositoryImpl(
        alarmLocalDataSource = alarmLocalDataSource,
    )

    @Test
    fun `should call getAlarmByTask from data source when getAlarmByTaskId is called from repository`() =
        runBlocking {
            val taskId = 45L
            val alarmData = makeAlarmData(
                taskId = taskId,
                repeatType = RepeatType.WEEK.ordinal,
                nextAlarmDate = "2022-02-23 08:40:00",
            )
            prepareScenario(alarmData = alarmData)

            val result = alarmRepository.getAlarmByTaskId(taskId)

            val expected = Alarm(
                dateTime = with(Calendar.getInstance()) {
                    set(Calendar.YEAR, 2022)
                    set(Calendar.MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.DAY_OF_MONTH, 23)
                    set(Calendar.MINUTE, 40)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    this
                },
                repeatType = RepeatType.WEEK,
            )
            assertEquals(expected, result)
            coVerify { alarmLocalDataSource.getAlarmByTask(taskId) }
        }

    @Test
    fun `should call deleteByTask from data source when deleteAlarmByTask is called from repository`() =
        runBlocking {
            val taskId = 45L
            prepareScenario()

            alarmRepository.deleteAlarmByTask(taskId)

            coVerify { alarmLocalDataSource.deleteByTask(taskId) }
        }

    @Test
    fun `should call save from data source when save is called from repository`() =
        runBlocking {
            val taskId = 45L
            val alarmData = makeAlarmData(
                id = -1,
                taskId = taskId,
                repeatType = RepeatType.WEEK.ordinal,
                nextAlarmDate = "2022-02-23 08:40:00",
                insertingDate = null,
            )
            val alarm = makeAlarm(
                dateTime = with(Calendar.getInstance()) {
                    set(Calendar.YEAR, 2022)
                    set(Calendar.MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.DAY_OF_MONTH, 23)
                    set(Calendar.MINUTE, 40)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    this
                },
                repeatType = RepeatType.WEEK,
            )
            prepareScenario()

            alarmRepository.save(alarm, taskId)

            coVerify { alarmLocalDataSource.save(alarmData) }
        }

    private fun prepareScenario(
        alarmData: AlarmData? = makeAlarmData(),
    ) {
        coEvery { alarmLocalDataSource.getAlarmByTask(any()) } returns alarmData
        coEvery { alarmLocalDataSource.deleteByTask(any()) } just runs
        coEvery { alarmLocalDataSource.save(any()) } just runs
    }

}