package br.com.sailboat.todozy.features.tasks.presentation.mapper

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.uicomponent.model.AlarmUiModel
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName

class AlarmToAlarmUiModelMapper(
    // TODO: Add string provider
    private val context: Context,
) {

    fun map(alarm: Alarm): AlarmUiModel {

        val date = alarm.dateTime.getFullDateName(context)
        val time = alarm.dateTime.formatTimeWithAndroidFormat(context)

        return AlarmUiModel(
            date = date,
            time = time,
            description = mapDescription(alarm.repeatType),
            isCustom = alarm.repeatType == RepeatType.CUSTOM,
            shouldRepeat = alarm.repeatType != RepeatType.NOT_REPEAT,
            customDays = alarm.customDays,
        )
    }

    private fun mapDescription(repeatType: RepeatType): String {
        return when (repeatType) {
            RepeatType.NOT_REPEAT -> R.string.not_repeat
            RepeatType.DAY -> R.string.every_day
            RepeatType.WEEK -> R.string.every_week
            RepeatType.MONTH -> R.string.every_month
            RepeatType.YEAR -> R.string.every_year
            RepeatType.SECOND -> R.string.every_second
            RepeatType.MINUTE -> R.string.every_minute
            RepeatType.HOUR -> R.string.every_hour
            RepeatType.CUSTOM -> R.string.custom
        }.run { context.getString(this) }
    }

}