package br.com.sailboat.todozy.domain.model

import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import java.util.*

data class Alarm(
    var dateTime: Calendar,
    var repeatType: RepeatType,
    var customDays: String? = null
) {

    fun isAlarmRepeating() = (repeatType != RepeatType.NOT_REPEAT)

    override fun toString(): String {
        return "dateTime: ${dateTime.toDateTimeString()}, repeatType: $repeatType" +
                (customDays?.takeIf { it.isNotEmpty() }?.run {
                    ", customDays: $customDays"
                }.orEmpty())
    }

}