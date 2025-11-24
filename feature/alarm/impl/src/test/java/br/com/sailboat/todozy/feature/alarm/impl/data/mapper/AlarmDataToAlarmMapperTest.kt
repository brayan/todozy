package br.com.sailboat.todozy.feature.alarm.impl.data.mapper

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.impl.data.model.AlarmData
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class AlarmDataToAlarmMapperTest {
    private val mapper = AlarmDataToAlarmMapper()

    @Test
    fun `maps alarm when nextAlarmDate is valid`() {
        val alarmDate = "2024-06-18 12:30:00"
        val alarmData =
            AlarmData(
                taskId = 12L,
                repeatType = RepeatType.DAY.ordinal,
                nextAlarmDate = alarmDate,
                insertingDate = null,
                days = null,
            )

        val result = mapper.map(alarmData)

        requireNotNull(result)
        assertEquals(RepeatType.DAY, result.repeatType)
        assertTrue(result.customDays.isNullOrEmpty())
        assertEquals(2024, result.dateTime.get(Calendar.YEAR))
        assertEquals(5, result.dateTime.get(Calendar.MONTH))
        assertEquals(18, result.dateTime.get(Calendar.DAY_OF_MONTH))
        assertEquals(12, result.dateTime.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, result.dateTime.get(Calendar.MINUTE))
    }

    @Test
    fun `returns null when nextAlarmDate is missing`() {
        val alarmData =
            AlarmData(
                taskId = 12L,
                repeatType = RepeatType.DAY.ordinal,
                nextAlarmDate = null,
                insertingDate = null,
                days = null,
            )

        val result = mapper.map(alarmData)

        assertNull(result)
    }

    @Test
    fun `returns null when nextAlarmDate cannot be parsed`() {
        val alarmData =
            AlarmData(
                taskId = 12L,
                repeatType = RepeatType.DAY.ordinal,
                nextAlarmDate = "bad-date",
                insertingDate = null,
                days = null,
            )

        val result = mapper.map(alarmData)

        assertNull(result)
    }
}
