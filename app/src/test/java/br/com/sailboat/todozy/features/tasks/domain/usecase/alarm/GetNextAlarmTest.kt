package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class GetNextAlarmTest {

    private val getNextAlarm = GetNextAlarm()

    @Test
    fun `should get next valid alarm when repeatType is SECOND`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.SECOND))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.SECOND, 1) },
                repeatType = RepeatType.SECOND))
    }

    @Test
    fun `should get next valid alarm when repeatType is MINUTE`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.MINUTE))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.MINUTE, 1) },
                repeatType = RepeatType.MINUTE))
    }

    @Test
    fun `should get next valid alarm when repeatType is HOUR`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.HOUR))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.HOUR, 1) },
                repeatType = RepeatType.HOUR))
    }

    @Test
    fun `should get next valid alarm when repeatType is DAY`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.DAY))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.DATE, 1) },
                repeatType = RepeatType.DAY))
    }

    @Test
    fun `should get next valid alarm when repeatType is WEEK`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.WEEK))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.WEEK_OF_YEAR, 1) },
                repeatType = RepeatType.WEEK))
    }

    @Test
    fun `should get next valid alarm when repeatType is MONTH`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.MONTH))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.MONTH, 1) },
                repeatType = RepeatType.MONTH))
    }

    @Test
    fun `should get next valid alarm when repeatType is YEAR`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.YEAR))
        assertEquals(result, Alarm(dateTime = dateTime.apply { add(Calendar.YEAR, 1) },
                repeatType = RepeatType.YEAR))
    }

    @Test
    fun `should get the same alarm when repeatType is NOT_REPEAT`() = runBlocking {
        val dateTime = Calendar.getInstance()

        val result = getNextAlarm(Alarm(dateTime = dateTime, repeatType = RepeatType.NOT_REPEAT))
        assertEquals(result, Alarm(dateTime = dateTime,
                repeatType = RepeatType.NOT_REPEAT))
    }

}