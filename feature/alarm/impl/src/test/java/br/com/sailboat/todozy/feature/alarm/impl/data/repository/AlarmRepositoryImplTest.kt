package br.com.sailboat.todozy.feature.alarm.impl.data.repository

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.domain.factory.AlarmMockFactory.makeAlarm
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.impl.data.factory.AlarmDataMockFactory.makeAlarmData
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.model.AlarmData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class AlarmRepositoryImplTest {

    private val alarmLocalDataSource: AlarmLocalDataSource = mockk(relaxed = true)
    private val alarmDataToAlarmMapper: AlarmDataToAlarmMapper = mockk(relaxed = true)
    private val alarmToAlarmDataMapper: AlarmToAlarmDataMapper = mockk(relaxed = true)

    private val alarmRepository = AlarmRepositoryImpl(
        alarmLocalDataSource = alarmLocalDataSource,
        alarmDataToAlarmMapper = alarmDataToAlarmMapper,
        alarmToAlarmDataMapper = alarmToAlarmDataMapper,
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
            val alarm = Alarm(
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
            val alarmResult = Result.success(alarm)
            prepareScenario(
                alarmData = alarmData,
                alarm = alarm,
            )

            val result = alarmRepository.getAlarmByTaskId(taskId)

            assertEquals(alarmResult, result)
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
            prepareScenario(alarmData = alarmData)

            alarmRepository.save(alarm, taskId)

            coVerify { alarmLocalDataSource.save(alarmData) }
        }

    private fun prepareScenario(
        alarm: Alarm = makeAlarm(),
        alarmData: AlarmData = makeAlarmData(),
        alarmDataResult: Result<AlarmData> = Result.success(makeAlarmData()),
        deleteByTaskResult: Result<Unit?> = Result.success(Unit),
        saveResult: Result<Long> = Result.success(42L),
    ) {
        coEvery { alarmLocalDataSource.getAlarmByTask(any()) } returns alarmDataResult
        coEvery { alarmLocalDataSource.deleteByTask(any()) } returns deleteByTaskResult
        coEvery { alarmLocalDataSource.save(any()) } returns saveResult
        coEvery { alarmDataToAlarmMapper.map(any()) } returns alarm
        coEvery { alarmToAlarmDataMapper.map(any(), any()) } returns alarmData
    }
}
