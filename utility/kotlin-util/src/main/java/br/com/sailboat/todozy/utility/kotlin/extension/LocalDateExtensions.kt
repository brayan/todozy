package br.com.sailboat.todozy.utility.kotlin.extension

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.GregorianCalendar

fun LocalDate.toStartOfDayCalendar(zoneId: ZoneId = ZoneId.systemDefault()): Calendar {
    val zonedDateTime = atStartOfDay(zoneId)
    return GregorianCalendar.from(zonedDateTime)
}

fun LocalDate.toEndOfDayCalendar(zoneId: ZoneId = ZoneId.systemDefault()): Calendar {
    val endOfDay = atTime(LocalTime.of(23, 59, 59)).atZone(zoneId)
    return GregorianCalendar.from(endOfDay)
}
