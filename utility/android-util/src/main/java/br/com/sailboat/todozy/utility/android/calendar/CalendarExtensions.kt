package br.com.sailboat.todozy.utility.android.calendar

import android.content.Context
import android.text.format.DateFormat
import br.com.sailboat.todozy.utility.android.R
import br.com.sailboat.todozy.utility.kotlin.extension.getDayName
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import java.text.SimpleDateFormat
import java.util.Calendar

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
    return SimpleDateFormat(format).format(time)
}

fun Calendar.getMonthAndDayShort(ctx: Context): String {
    val pattern = ctx.getString(R.string.pattern_month_day_short)
    return SimpleDateFormat(pattern).format(time)
}

fun Calendar.getMonthAndDayLong(ctx: Context): String {
    val pattern = ctx.getString(R.string.pattern_month_day_long)
    return SimpleDateFormat(pattern).format(time)
}

fun Calendar.getMonthDayAndYearShort(ctx: Context): String {
    val pattern = ctx.getString(R.string.pattern_month_day_year_short)
    return SimpleDateFormat(pattern).format(time)
}
