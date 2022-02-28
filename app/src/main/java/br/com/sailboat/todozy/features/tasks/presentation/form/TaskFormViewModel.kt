package br.com.sailboat.todozy.features.tasks.presentation.form

import br.com.sailboat.todozy.feature.alarm.domain.model.RepeatType
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import java.util.*

class TaskFormViewModel {

    var taskId = Entity.NO_ID
    var name = ""
    var notes = ""
    var alarm: Calendar? = null
    var repeatAlarmType = RepeatType.NOT_REPEAT
    var selectedDays: String? = null

}