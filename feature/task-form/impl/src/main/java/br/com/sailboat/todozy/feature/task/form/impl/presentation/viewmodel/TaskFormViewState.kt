package br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.model.AlarmForm
import br.com.sailboat.todozy.utility.android.livedata.Event
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import java.util.Calendar

internal class TaskFormViewState {
    val viewAction = Event<TaskFormViewAction>()
    var taskId: Long = Entity.NO_ID
    var alarm: Calendar? = null
    var repeatAlarmType: RepeatType = RepeatType.NOT_REPEAT
    var selectedDays: String? = null
    val alarmForm = MutableLiveData<AlarmForm>()
    val isEditingTask = MutableLiveData<Boolean>()
}
