package br.com.sailboat.todozy.core.presentation.helper

import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType

enum class RepeatTypeView(val repeatType: RepeatType, val description: Int) {

    NOT_REPEAT(RepeatType.NOT_REPEAT, R.string.not_repeat),
    DAY(RepeatType.DAY, R.string.every_day),
    WEEK(RepeatType.WEEK, R.string.every_week),
    MONTH(RepeatType.MONTH, R.string.every_month),
    YEAR(RepeatType.YEAR, R.string.every_year),
    SECOND(RepeatType.SECOND, R.string.every_second),
    MINUTE(RepeatType.MINUTE, R.string.every_minute),
    HOUR(RepeatType.HOUR, R.string.every_hour),
    CUSTOM(RepeatType.CUSTOM, R.string.custom);

    companion object {
        fun getFromRepeatType(repeatType: RepeatType): RepeatTypeView {
            values().forEach {
                if (it.repeatType == repeatType) {
                    return it
                }
            }
            throw Exception("RepeatType not found.")
        }
    }

}