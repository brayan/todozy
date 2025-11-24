package br.com.sailboat.todozy.feature.alarm.impl.presentation.formatter

import android.content.Context
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import java.util.Calendar

internal class AlarmDateTimeFormatterImpl(
    private val context: Context,
) : AlarmDateTimeFormatter {

    override fun formatDate(calendar: Calendar): String = calendar.getFullDateName(context)

    override fun formatTime(calendar: Calendar): String = calendar.formatTimeWithAndroidFormat(context)
}
