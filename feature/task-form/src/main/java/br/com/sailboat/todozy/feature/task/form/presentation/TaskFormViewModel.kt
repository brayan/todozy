package br.com.sailboat.todozy.feature.task.form.presentation

import br.com.sailboat.todozy.domain.model.RepeatType
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