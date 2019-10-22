package br.com.sailboat.todozy.ui.task.insert

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.ui.dialog.selectable.RepeatAlarmSelectableItem
import java.util.*

class InsertTaskViewModel {

    var taskId = EntityHelper.NO_ID
    var name = ""
    var notes = ""
    var alarm: Calendar? = null
    var repeatAlarmType: RepeatType? = null
    var selectedDays: String? = null

}