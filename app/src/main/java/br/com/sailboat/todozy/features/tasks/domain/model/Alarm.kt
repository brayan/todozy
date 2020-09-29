package br.com.sailboat.todozy.features.tasks.domain.model

import br.com.sailboat.todozy.core.extensions.toDateTimeString
import java.util.*

data class Alarm(var dateTime: Calendar,
                 var repeatType: RepeatType,
                 var customDays: String? = null) {

    fun isAlarmRepeating() = (repeatType != RepeatType.NOT_REPEAT)

    override fun toString(): String {
        return "dateTime: ${dateTime.toDateTimeString()}, repeatType: $repeatType" +
                (customDays?.takeIf { it.isNotEmpty() }?.run { ", customDays: $customDays" } ?: "")
    }

}