package br.com.sailboat.todozy.feature.task.history.impl.domain.service

import java.util.Calendar

internal interface CalendarService {
    fun getShortDate(calendar: Calendar): String
}
