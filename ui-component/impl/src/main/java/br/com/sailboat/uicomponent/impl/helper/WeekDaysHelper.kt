package br.com.sailboat.uicomponent.impl.helper

import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.model.DayUiModel
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import java.util.Calendar

class WeekDaysHelper(private val stringProvider: StringProvider) {

    fun getCustomRepeat(days: String?): String {
        var days = days
        if (days.isNullOrEmpty()) {
            return stringProvider.getString(R.string.custom)
        } else {
            days = orderStartingWithSunday(days)

            val sb = StringBuilder()
            sb.append(getDayViewFromId(Integer.valueOf(days[0].toString())).name)

            if (days.length > 1) {
                for (i in 1 until days.length) {

                    if (days.length > 2 && i != days.length - 1) {
                        sb.append(", ")
                    } else {
                        sb.append(" ")
                        sb.append(stringProvider.getString(R.string.and))
                        sb.append(" ")
                    }

                    sb.append(getDayViewFromId(Integer.valueOf(days[i].toString())).name)
                }
            }

            return sb.toString()
        }
    }

    fun getDayViewFromId(id: Int): DayUiModel {
        when (id) {
            Calendar.SUNDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.sunday))
            }
            Calendar.MONDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.monday))
            }
            Calendar.TUESDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.tuesday))
            }
            Calendar.WEDNESDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.wednesday))
            }
            Calendar.THURSDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.thursday))
            }
            Calendar.FRIDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.friday))
            }
            Calendar.SATURDAY -> {
                return DayUiModel(id, stringProvider.getString(R.string.saturday))
            }
            else -> {
                throw IllegalArgumentException("Invalid Day ID")
            }
        }
    }

    fun orderStartingWithSunday(days: String): String {
        val sb = StringBuilder()

        if (days.contains(Calendar.SUNDAY.toString())) {
            sb.append(Calendar.SUNDAY.toString())
        }

        if (days.contains(Calendar.MONDAY.toString())) {
            sb.append(Calendar.MONDAY.toString())
        }

        if (days.contains(Calendar.TUESDAY.toString())) {
            sb.append(Calendar.TUESDAY.toString())
        }

        if (days.contains(Calendar.WEDNESDAY.toString())) {
            sb.append(Calendar.WEDNESDAY.toString())
        }

        if (days.contains(Calendar.THURSDAY.toString())) {
            sb.append(Calendar.THURSDAY.toString())
        }

        if (days.contains(Calendar.FRIDAY.toString())) {
            sb.append(Calendar.FRIDAY.toString())
        }

        if (days.contains(Calendar.SATURDAY.toString())) {
            sb.append(Calendar.SATURDAY.toString())
        }

        return sb.toString()
    }
}
