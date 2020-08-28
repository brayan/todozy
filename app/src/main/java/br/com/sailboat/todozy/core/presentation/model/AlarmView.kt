package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.core.presentation.helper.RepeatTypeView
import java.util.*

data class AlarmView(var dateTime: Calendar,
                     var repeatType: RepeatTypeView,
                     var customDays: String?,
                     override val viewType: Int = ViewType.ALARM.ordinal) : ItemView