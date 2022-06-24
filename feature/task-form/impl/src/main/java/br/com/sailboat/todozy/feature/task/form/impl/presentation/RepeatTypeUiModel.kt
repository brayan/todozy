package br.com.sailboat.todozy.feature.task.form.impl.presentation

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.R

internal enum class RepeatTypeUiModel(val repeatType: RepeatType, val description: Int) {

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
        fun getFromRepeatType(repeatType: RepeatType): RepeatTypeUiModel {
            values().forEach {
                if (it.repeatType == repeatType) {
                    return it
                }
            }
            throw Exception("RepeatType not found.")
        }
    }
}
