package br.com.sailboat.todozy.feature.task.history.impl.data.service

import android.content.Context
import br.com.sailboat.todozy.feature.task.history.impl.domain.service.CalendarService
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import java.util.Calendar

internal class CalendarServiceImpl(
    private val context: Context
) : CalendarService {

    override fun getShortDate(calendar: Calendar): String {
        return calendar.toShortDateView(context)
    }
}
