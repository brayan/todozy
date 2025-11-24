package br.com.sailboat.todozy.feature.task.form.impl.presentation

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.uicomponent.impl.R as UiR

internal enum class RepeatTypeUiModel(val repeatType: RepeatType, val description: Int) {

    NOT_REPEAT(RepeatType.NOT_REPEAT, UiR.string.not_repeat),
    DAY(RepeatType.DAY, UiR.string.every_day),
    WEEK(RepeatType.WEEK, UiR.string.every_week),
    MONTH(RepeatType.MONTH, UiR.string.every_month),
    YEAR(RepeatType.YEAR, UiR.string.every_year),
    SECOND(RepeatType.SECOND, UiR.string.every_second),
    MINUTE(RepeatType.MINUTE, UiR.string.every_minute),
    HOUR(RepeatType.HOUR, UiR.string.every_hour),
    CUSTOM(RepeatType.CUSTOM, UiR.string.custom);

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
