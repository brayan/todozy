package br.com.sailboat.todozy.feature.task.form.impl.presentation.extension

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

internal class CalendarExtensionsTest {
    private var defaultTimeZone: TimeZone? = null

    @Before
    fun setUp() {
        defaultTimeZone = TimeZone.getDefault()
    }

    @After
    fun tearDown() {
        defaultTimeZone?.let { TimeZone.setDefault(it) }
    }

    @Test
    fun `toUtcStartOfDayInMillis keeps local date when timezone pushes time past utc midnight`() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+10:00"))
        val localCalendar =
            Calendar.getInstance().apply {
                set(2024, Calendar.APRIL, 10, 23, 45, 0)
                set(Calendar.MILLISECOND, 0)
            }

        val utcStartOfDayMillis = localCalendar.toUtcStartOfDayInMillis()

        val utcCalendar =
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = utcStartOfDayMillis
            }

        assertEquals(2024, utcCalendar.get(Calendar.YEAR))
        assertEquals(Calendar.APRIL, utcCalendar.get(Calendar.MONTH))
        assertEquals(10, utcCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, utcCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, utcCalendar.get(Calendar.MINUTE))
    }
}
