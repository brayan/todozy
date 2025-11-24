package br.com.sailboat.uicomponent.impl.formatter

import android.content.Context
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import br.com.sailboat.todozy.utility.android.calendar.getMonthAndDayShort
import br.com.sailboat.todozy.utility.android.calendar.getMonthDayAndYearShort
import br.com.sailboat.todozy.utility.kotlin.extension.isCurrentYear
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isYesterday
import java.util.Calendar

class TaskHistoryDateTimeFormatterImpl(
    private val context: Context,
) : TaskHistoryDateTimeFormatter {
    override fun formatFull(calendar: Calendar): String {
        val formattedTime = calendar.formatTimeWithAndroidFormat(context)
        val formattedDate = calendar.getFullDateName(context)
        return "$formattedTime - $formattedDate"
    }

    override fun formatShort(calendar: Calendar): String {
        return when {
            calendar.isToday() || calendar.isYesterday() -> calendar.formatTimeWithAndroidFormat(context)
            calendar.isCurrentYear() -> calendar.getMonthAndDayShort(context)
            else -> calendar.getMonthDayAndYearShort(context)
        }
    }
}
