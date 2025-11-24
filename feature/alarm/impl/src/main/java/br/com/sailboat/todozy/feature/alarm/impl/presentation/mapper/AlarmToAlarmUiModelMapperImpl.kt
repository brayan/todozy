package br.com.sailboat.todozy.feature.alarm.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.impl.presentation.formatter.AlarmDateTimeFormatter
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import br.com.sailboat.uicomponent.impl.helper.WeekDaysHelper
import br.com.sailboat.uicomponent.model.AlarmUiModel
import br.com.sailboat.uicomponent.impl.R as UiR

internal class AlarmToAlarmUiModelMapperImpl(
    private val formatter: AlarmDateTimeFormatter,
    private val stringProvider: StringProvider,
    private val weekDaysHelper: WeekDaysHelper,
) : AlarmToAlarmUiModelMapper {
    override fun map(alarm: Alarm): AlarmUiModel {
        val date = formatter.formatDate(alarm.dateTime)
        val time = formatter.formatTime(alarm.dateTime)

        return AlarmUiModel(
            date = date,
            time = time,
            description = mapRepeatTypeDescription(alarm.repeatType, alarm.customDays),
            isCustom = alarm.repeatType == RepeatType.CUSTOM,
            shouldRepeat = alarm.repeatType != RepeatType.NOT_REPEAT,
            customDays = alarm.customDays,
        )
    }

    private fun mapRepeatTypeDescription(
        repeatType: RepeatType,
        customDays: String?,
    ): String {
        if (repeatType == RepeatType.CUSTOM && !customDays.isNullOrEmpty()) {
            return weekDaysHelper.getCustomRepeat(customDays)
        }

        val descriptionId =
            when (repeatType) {
                RepeatType.NOT_REPEAT -> UiR.string.not_repeat
                RepeatType.DAY -> UiR.string.every_day
                RepeatType.WEEK -> UiR.string.every_week
                RepeatType.MONTH -> UiR.string.every_month
                RepeatType.YEAR -> UiR.string.every_year
                RepeatType.SECOND -> UiR.string.every_second
                RepeatType.MINUTE -> UiR.string.every_minute
                RepeatType.HOUR -> UiR.string.every_hour
                RepeatType.CUSTOM -> UiR.string.custom
            }
        return stringProvider.getString(descriptionId)
    }
}
