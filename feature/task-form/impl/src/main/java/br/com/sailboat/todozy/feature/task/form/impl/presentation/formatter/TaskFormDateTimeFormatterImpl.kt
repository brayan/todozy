package br.com.sailboat.todozy.feature.task.form.impl.presentation.formatter

import android.content.Context
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import java.util.Calendar

internal class TaskFormDateTimeFormatterImpl(
    private val context: Context,
) : TaskFormDateTimeFormatter {

    override fun formatDate(calendar: Calendar): String = calendar.getFullDateName(context)

    override fun formatTime(calendar: Calendar): String = calendar.formatTimeWithAndroidFormat(context)
}
