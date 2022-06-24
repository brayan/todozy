package br.com.sailboat.todozy.feature.task.form.impl.domain.service

import br.com.sailboat.todozy.domain.model.RepeatType
import java.util.Calendar

internal interface AlarmService {
    fun getRepeatTypeDescription(repeatType: RepeatType): String
    fun getCustomRepeatTypeDescription(days: String): String
    fun getFullDate(calendar: Calendar): String
    fun getFullTime(calendar: Calendar): String
}
