package br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.model.AlarmForm
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.helper.Event
import java.util.Calendar

internal class TaskFormViewState {
    val action = Event<Action>()
    var taskId: Long = Entity.NO_ID

    var alarm: Calendar? = null
    var repeatAlarmType: RepeatType = RepeatType.NOT_REPEAT
    var selectedDays: String? = null
    val alarmForm = MutableLiveData<AlarmForm>()
    val isEditingTask = MutableLiveData<Boolean>()

    sealed class Action {
        data class SetTaskDetails(
            val taskName: String?,
            val taskNotes: String?,
        ) : Action()

        data class CloseTaskForm(val success: Boolean) : Action()
        data class NavigateToAlarmDateSelector(val currentDate: Calendar) : Action()
        data class NavigateToAlarmTimeSelector(val currentTime: Calendar) : Action()
        data class NavigateToRepeatAlarmSelector(val repeatType: RepeatType) : Action()
        data class NavigateToCustomRepeatAlarmSelector(val days: String?) : Action()
        object SetFocusOnInputTaskName : Action()
        object ShowErrorSavingTask : Action()
        object HideKeyboard : Action()
        object ShowErrorTaskNameCantBeEmpty : Action()
        object ShowErrorAlarmNotValid : Action()
    }
}
