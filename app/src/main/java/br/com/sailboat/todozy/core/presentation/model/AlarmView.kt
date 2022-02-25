package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.core.presentation.helper.RepeatTypeView
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.UiModelType
import java.util.*

data class AlarmView(
    var dateTime: Calendar,
    var repeatType: RepeatTypeView,
    var customDays: String?,
    override val index: Int = UiModelType.ALARM.ordinal
) : UiModel