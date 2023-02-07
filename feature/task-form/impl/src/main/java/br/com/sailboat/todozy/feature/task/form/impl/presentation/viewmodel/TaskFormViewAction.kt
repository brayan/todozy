package br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.RepeatType
import java.util.Calendar

internal sealed class TaskFormViewAction {
    object SetFocusOnInputTaskName : TaskFormViewAction()
    object ShowErrorSavingTask : TaskFormViewAction()
    object HideKeyboard : TaskFormViewAction()
    object ShowErrorTaskNameCantBeEmpty : TaskFormViewAction()
    object ShowErrorAlarmNotValid : TaskFormViewAction()
    data class SetTaskDetails(
        val taskName: String?,
        val taskNotes: String?,
    ) : TaskFormViewAction()
    data class CloseTaskForm(val success: Boolean) : TaskFormViewAction()
    data class NavigateToAlarmDateSelector(val currentDate: Calendar) : TaskFormViewAction()
    data class NavigateToAlarmTimeSelector(val currentTime: Calendar) : TaskFormViewAction()
    data class NavigateToRepeatAlarmSelector(val repeatType: RepeatType) : TaskFormViewAction()
    data class NavigateToCustomRepeatAlarmSelector(val days: String?) : TaskFormViewAction()
}
