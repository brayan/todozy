package br.com.sailboat.todozy.domain.model

import java.util.Calendar

data class Alarm(
    var dateTime: Calendar,
    var repeatType: RepeatType,
    var customDays: String? = null
) {

    fun isAlarmRepeating() = (repeatType != RepeatType.NOT_REPEAT)
}
