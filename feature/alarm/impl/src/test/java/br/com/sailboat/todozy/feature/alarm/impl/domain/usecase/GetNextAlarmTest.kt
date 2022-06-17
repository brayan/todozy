package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class GetNextAlarmTest {

    private val getNextAlarm = GetNextAlarm()

    @Test
    fun `should get next valid alarm when repeatType is SECOND`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.SECOND))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.SECOND, 1) },
            repeatType = RepeatType.SECOND,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get next valid alarm when repeatType is MINUTE`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.MINUTE))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.MINUTE, 1) },
            repeatType = RepeatType.MINUTE,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get next valid alarm when repeatType is HOUR`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.HOUR))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.HOUR, 1) },
            repeatType = RepeatType.HOUR,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get next valid alarm when repeatType is DAY`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.DAY))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.DAY,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get next valid alarm when repeatType is WEEK`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.WEEK_OF_YEAR, 1) },
            repeatType = RepeatType.WEEK,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get next valid alarm when repeatType is MONTH`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.MONTH))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.MONTH, 1) },
            repeatType = RepeatType.MONTH,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get next valid alarm when repeatType is YEAR`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.YEAR))

        val expected = Alarm(
            dateTime = dateTime.apply { add(Calendar.YEAR, 1) },
            repeatType = RepeatType.YEAR,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should get the same alarm when repeatType is NOT_REPEAT`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.NOT_REPEAT))

        val expected = Alarm(
            dateTime = dateTime,
            repeatType = RepeatType.NOT_REPEAT,
        )
        assertEquals(expected, result)
    }
}
