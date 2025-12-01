package br.com.sailboat.uicomponent.impl.progress

import br.com.sailboat.todozy.domain.model.TaskProgressDay
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

internal class TaskProgressGridBuilderTest {
    @Test
    fun `should not add extra cells for seven day range starting midweek`() {
        val startDate = LocalDate.of(2024, 3, 6) // Wednesday
        val days = createProgressDays(startDate, 7)

        val result = buildWeekColumns(days, DefaultTaskProgressDayOrder)

        assertEquals(2, result.size)
        assertEquals(2, result.first().offset)
        assertEquals(7, result.sumOf { it.days.size })
        assertEquals(
            days.map { it.date },
            result.flatMap { it.days }.map { it.date },
        )
    }

    @Test
    fun `should keep exact day count for thirty day range`() {
        val startDate = LocalDate.of(2024, 2, 4) // Sunday
        val days = createProgressDays(startDate, 30)

        val result = buildWeekColumns(days, DefaultTaskProgressDayOrder)

        assertEquals(30, result.sumOf { it.days.size })
        assertEquals(6, result.first().offset)
        assertEquals(
            days.first().date,
            result.first().days.first().date,
        )
        assertEquals(
            days.last().date,
            result.last().days.last().date,
        )
    }

    private fun createProgressDays(
        start: LocalDate,
        count: Int,
    ): List<TaskProgressDay> =
        (0 until count).map { offset ->
            val date = start.plusDays(offset.toLong())
            TaskProgressDay(
                date = date,
                doneCount = 1,
                notDoneCount = 0,
                totalCount = 1,
            )
        }
}
