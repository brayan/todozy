package br.com.sailboat.todozy.feature.task.form.impl.domain.service

import android.content.Context
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.RepeatTypeUiModel
import br.com.sailboat.uicomponent.impl.helper.WeekDaysHelper
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import java.util.*

class AlarmServiceImpl(private val context: Context) : AlarmService {

    override fun getRepeatTypeDescription(repeatType: RepeatType): String {
        val repeatTypeUiModel = RepeatTypeUiModel.getFromRepeatType(repeatType)
        return context.getString(repeatTypeUiModel.description)
    }

    override fun getCustomRepeatTypeDescription(days: String): String {
        return WeekDaysHelper().getCustomRepeat(context, days)
    }

    override fun getFullDate(calendar: Calendar): String {
        return calendar.getFullDateName(context)
    }

    override fun getFullTime(calendar: Calendar): String {
        return calendar.formatTimeWithAndroidFormat(context)
    }

}