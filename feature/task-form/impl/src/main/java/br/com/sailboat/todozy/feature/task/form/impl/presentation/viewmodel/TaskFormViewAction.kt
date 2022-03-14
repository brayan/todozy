package br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.RepeatType

sealed class TaskFormViewAction {
    data class OnStart(val taskId: Long) : TaskFormViewAction()
    data class OnClickSaveTask(val taskName: String, val taskNotes: String) : TaskFormViewAction()
    data class OnSelectAlarmDate(val year: Int, val month: Int, val day: Int) : TaskFormViewAction()
    data class OnSelectAlarmTime(val hourOfDay: Int, val minute: Int) : TaskFormViewAction()
    data class OnSelectAlarmType(val repeatType: RepeatType) : TaskFormViewAction()
    data class OnSelectCustomAlarmType(val days: String) : TaskFormViewAction()
    object OnClickAddAlarm : TaskFormViewAction()
    object OnClickAlarmDate : TaskFormViewAction()
    object OnClickAlarmTime : TaskFormViewAction()
    object OnClickRepeatAlarm : TaskFormViewAction()
    object OnClickCustomRepeatAlarm : TaskFormViewAction()
}