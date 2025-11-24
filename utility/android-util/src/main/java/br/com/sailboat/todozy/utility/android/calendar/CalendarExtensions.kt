package br.com.sailboat.todozy.utility.android.calendar

import android.content.Context
import android.text.format.DateFormat
import android.os.Build
import br.com.sailboat.todozy.utility.android.R
import br.com.sailboat.todozy.utility.kotlin.extension.getDayName
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun String.toShortDateView(ctx: Context): String {
    val calendar = this.toDateTimeCalendar()
    return calendar.toShortDateView(ctx)
}

fun Calendar.toShortDateView(ctx: Context): String {
    return DateFormat.getDateFormat(ctx).format(time)
}

fun Context.getDatePattern(): String {
    return if (DateFormat.is24HourFormat(this)) {
        "HH:mm"
    } else {
        "hh:mm a"
    }
}

fun Calendar.getFullDateName(ctx: Context): String {
    return if (isToday()) {
        ctx.getString(R.string.today) + ", " + DateFormat.getLongDateFormat(ctx).format(time)
    } else if (isTomorrow()) {
        ctx.getString(R.string.tomorrow) + ", " + DateFormat.getLongDateFormat(ctx).format(time)
    } else {
        getDayName() + ", " + DateFormat.getLongDateFormat(ctx).format(time)
    }
}

fun Calendar.getSimpleDayName(ctx: Context): String {
    return if (isToday()) {
        ctx.getString(R.string.today)
    } else if (isTomorrow()) {
        ctx.getString(R.string.tomorrow)
    } else {
        getFullDateName(ctx)
    }
}

fun Calendar.formatTimeWithAndroidFormat(ctx: Context): String {
    val format = ctx.getDatePattern()
    val locale = ctx.getPreferredLocale()
    return formatWithPattern(format, locale)
}

fun Calendar.getMonthAndDayShort(ctx: Context): String {
    val pattern = ctx.getString(R.string.pattern_month_day_short)
    return formatWithPattern(pattern, ctx.getPreferredLocale())
}

fun Calendar.getMonthAndDayLong(ctx: Context): String {
    val pattern = ctx.getString(R.string.pattern_month_day_long)
    return formatWithPattern(pattern, ctx.getPreferredLocale())
}

fun Calendar.getMonthDayAndYearShort(ctx: Context): String {
    val pattern = ctx.getString(R.string.pattern_month_day_year_short)
    return formatWithPattern(pattern, ctx.getPreferredLocale())
}

private fun Calendar.toLocalDateTime(): LocalDateTime {
    val zoneId = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(toInstant(), zoneId)
}

private fun Calendar.formatWithPattern(pattern: String, locale: Locale): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return formatter.format(toLocalDateTime())
}

private fun Context.getPreferredLocale(): Locale {
    val configuration = resources.configuration
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales[0]
    } else {
        configuration.locale
    }
}
