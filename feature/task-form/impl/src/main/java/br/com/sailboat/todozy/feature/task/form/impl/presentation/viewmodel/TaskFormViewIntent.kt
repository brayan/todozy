package br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.RepeatType

internal sealed class TaskFormViewIntent {
    data class OnStart(val taskId: Long) : TaskFormViewIntent()
    data class OnClickSaveTask(val taskName: String, val taskNotes: String) : TaskFormViewIntent()
    data class OnSelectAlarmDate(val year: Int, val month: Int, val day: Int) : TaskFormViewIntent()
    data class OnSelectAlarmTime(val hourOfDay: Int, val minute: Int) : TaskFormViewIntent()
    data class OnSelectAlarmType(val repeatType: RepeatType) : TaskFormViewIntent()
    data class OnSelectCustomAlarmType(val days: String) : TaskFormViewIntent()
    object OnClickAddAlarm : TaskFormViewIntent()
    object OnClickAlarmDate : TaskFormViewIntent()
    object OnClickAlarmTime : TaskFormViewIntent()
    object OnClickRepeatAlarm : TaskFormViewIntent()
    object OnClickCustomRepeatAlarm : TaskFormViewIntent()
}
