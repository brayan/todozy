package br.com.sailboat.todozy.core.extensions

import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import java.util.*

// TODO: REMOVE THIS EXTENSIONS AFTER REFACTORING
fun Calendar.incrementToNextValidDate(repeatType: RepeatType, days: String?) {
    do {
        incrementToNext(this, repeatType, days)
    } while (isBeforeNow())
}

fun incrementToNext(calendar: Calendar, repeatType: RepeatType, days: String?) {
    when (repeatType) {
        RepeatType.SECOND -> {
            calendar.add(Calendar.SECOND, 1)
            return
        }
        RepeatType.MINUTE -> {
            calendar.add(Calendar.MINUTE, 1)
            return
        }
        RepeatType.HOUR -> {
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            return
        }
        RepeatType.DAY -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            return
        }
        RepeatType.WEEK -> {
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
            return
        }
        RepeatType.MONTH -> {
            calendar.add(Calendar.MONTH, 1)
            return
        }
        RepeatType.YEAR -> {
            calendar.add(Calendar.YEAR, 1)
            return
        }
        RepeatType.CUSTOM -> {
            days?.run { incrementToNextCustomAlarm(calendar, days) }
            return
        }
    }
}

private fun incrementToNextCustomAlarm(calendar: Calendar, days: String) {
    var currentDayOfWeek: String?
    do {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).toString()

    } while (!days.contains(currentDayOfWeek!!))
}