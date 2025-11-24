package br.com.sailboat.todozy.feature.task.form.impl.presentation.formatter

import java.util.Calendar

internal interface TaskFormDateTimeFormatter {
    fun formatDate(calendar: Calendar): String
    fun formatTime(calendar: Calendar): String
}
