package br.com.sailboat.todozy.feature.task.form.impl.presentation.extension

import java.util.Calendar
import java.util.TimeZone

internal fun Calendar.toUtcStartOfDayInMillis(): Long {
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.clear()
    utcCalendar.set(
        get(Calendar.YEAR),
        get(Calendar.MONTH),
        get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0,
    )
    utcCalendar.set(Calendar.MILLISECOND, 0)
    return utcCalendar.timeInMillis
}
