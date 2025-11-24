package br.com.sailboat.todozy.feature.task.form.impl.domain.service

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.RepeatTypeUiModel
import br.com.sailboat.todozy.feature.task.form.impl.presentation.formatter.TaskFormDateTimeFormatter
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import br.com.sailboat.uicomponent.impl.helper.WeekDaysHelper
import java.util.Calendar

internal class AlarmServiceImpl(
    private val stringProvider: StringProvider,
    private val dateTimeFormatter: TaskFormDateTimeFormatter,
    private val weekDaysHelper: WeekDaysHelper,
) : AlarmService {

    override fun getRepeatTypeDescription(repeatType: RepeatType): String {
        val repeatTypeUiModel = RepeatTypeUiModel.getFromRepeatType(repeatType)
        return stringProvider.getString(repeatTypeUiModel.description)
    }

    override fun getCustomRepeatTypeDescription(days: String): String {
        return weekDaysHelper.getCustomRepeat(days)
    }

    override fun getFullDate(calendar: Calendar): String {
        return dateTimeFormatter.formatDate(calendar)
    }

    override fun getFullTime(calendar: Calendar): String {
        return dateTimeFormatter.formatTime(calendar)
    }
}
