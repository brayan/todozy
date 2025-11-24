package br.com.sailboat.todozy.feature.alarm.impl.presentation.formatter

import java.util.Calendar

internal interface AlarmDateTimeFormatter {
    fun formatDate(calendar: Calendar): String
    fun formatTime(calendar: Calendar): String
}
