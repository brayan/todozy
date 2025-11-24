package br.com.sailboat.uicomponent.impl.formatter

import java.util.Calendar

interface TaskHistoryDateTimeFormatter {
    fun formatFull(calendar: Calendar): String
    fun formatShort(calendar: Calendar): String
}
