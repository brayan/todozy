package br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFieldsConditions
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.form.impl.domain.service.AlarmService
import br.com.sailboat.todozy.feature.task.form.impl.domain.usecase.CheckTaskFieldsUseCase
import br.com.sailboat.todozy.feature.task.form.impl.presentation.model.AlarmForm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.NavigateToCustomRepeatAlarmSelector
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.NavigateToRepeatAlarmSelector
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialAlarm
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.launch
import java.util.Calendar

internal class TaskFormViewModel(
    override val viewState: TaskFormViewState = TaskFormViewState(),
    private val getTaskUseCase: GetTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getNextAlarmUseCase: GetNextAlarmUseCase,
    private val checkTaskFieldsUseCase: CheckTaskFieldsUseCase,
    private val logService: LogService,
    private val alarmService: AlarmService,
) : BaseViewModel<TaskFormViewState, TaskFormViewAction>() {

    override fun dispatchViewAction(viewAction: TaskFormViewAction) {
        when (viewAction) {
            is TaskFormViewAction.OnStart -> onStart(viewAction)
            is TaskFormViewAction.OnClickAddAlarm -> onClickAddAlarm()
            is TaskFormViewAction.OnClickSaveTask -> onClickSaveTask(viewAction)
            is TaskFormViewAction.OnSelectAlarmDate -> onSelectAlarmDate(viewAction)
            is TaskFormViewAction.OnSelectAlarmTime -> onSelectAlarmTime(viewAction)
            is TaskFormViewAction.OnSelectAlarmType -> onSelectAlarmType(viewAction)
            is TaskFormViewAction.OnSelectCustomAlarmType -> onSelectCustomAlarmType(viewAction)
            is TaskFormViewAction.OnClickAlarmDate -> onClickAlarmDate()
            is TaskFormViewAction.OnClickAlarmTime -> onClickAlarmTime()
            is TaskFormViewAction.OnClickRepeatAlarm -> onClickRepeatAlarm()
            is TaskFormViewAction.OnClickCustomRepeatAlarm -> onClickCustomRepeatAlarm()
        }
    }

    private fun onStart(viewAction: TaskFormViewAction.OnStart) {
        viewState.taskId = viewAction.taskId

        if (hasTaskToEdit()) {
            viewState.isEditingTask.value = true
            startEditingTask()
        } else {
            viewState.isEditingTask.value = false
            viewState.action.value = TaskFormViewState.Action.SetFocusOnInputTaskName
            updateAlarm()
        }
    }

    private fun onClickAddAlarm() {
        viewState.action.value = TaskFormViewState.Action.HideKeyboard

        if (hasAlarm()) {
            clearAlarm()
        } else {
            initAlarm()
        }

        updateAlarm(animate = true)
    }

    private fun onClickSaveTask(viewAction: TaskFormViewAction.OnClickSaveTask) {
        try {
            viewState.action.value = TaskFormViewState.Action.HideKeyboard
            checkFieldsAndSaveTask(viewAction.taskName, viewAction.taskNotes)
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = TaskFormViewState.Action.ShowErrorSavingTask
        }
    }

    private fun onSelectAlarmDate(viewAction: TaskFormViewAction.OnSelectAlarmDate) {
        viewState.alarm?.apply {
            set(Calendar.YEAR, viewAction.year)
            set(Calendar.MONTH, viewAction.month)
            set(Calendar.DAY_OF_MONTH, viewAction.day)
        }
        updateAlarm()
    }

    private fun onSelectAlarmTime(viewAction: TaskFormViewAction.OnSelectAlarmTime) {
        viewState.alarm?.apply {
            set(Calendar.HOUR_OF_DAY, viewAction.hourOfDay)
            set(Calendar.MINUTE, viewAction.minute)
        }
        updateAlarm()
    }

    private fun onSelectAlarmType(viewAction: TaskFormViewAction.OnSelectAlarmType) {
        viewState.repeatAlarmType = viewAction.repeatType
        viewState.selectedDays = null
        updateAlarm()
    }

    private fun onSelectCustomAlarmType(viewAction: TaskFormViewAction.OnSelectCustomAlarmType) {
        viewState.selectedDays = null

        when (viewAction.days.length) {
            1 -> {
                viewState.selectedDays = viewAction.days
                viewState.repeatAlarmType = RepeatType.CUSTOM

                val dayOfWeek = viewState.alarm?.get(Calendar.DAY_OF_WEEK).toString()
                if (viewAction.days.contains(dayOfWeek).not()) {
                    val alarm = createAlarmFromViews()
                    viewState.alarm = alarm?.let { getNextAlarmUseCase(it).dateTime }
                }

                viewState.selectedDays = null
                viewState.repeatAlarmType = RepeatType.WEEK
            }
            7 -> {
                viewState.repeatAlarmType = RepeatType.DAY
            }
            else -> {
                viewState.selectedDays = viewAction.days
                viewState.repeatAlarmType = RepeatType.CUSTOM

                val dayOfWeek = viewState.alarm?.get(Calendar.DAY_OF_WEEK).toString()
                if (viewAction.days.contains(dayOfWeek).not()) {
                    val alarm = createAlarmFromViews()
                    viewState.alarm = alarm?.let { getNextAlarmUseCase(it).dateTime }
                }
            }
        }

        updateAlarm()
    }

    private fun onClickAlarmDate() {
        viewState.alarm?.let { alarm ->
            viewState.action.value = TaskFormViewState.Action.NavigateToAlarmDateSelector(alarm)
        }
    }

    private fun onClickAlarmTime() {
        viewState.alarm?.let { alarm ->
            viewState.action.value = TaskFormViewState.Action.NavigateToAlarmTimeSelector(alarm)
        }
    }

    private fun onClickRepeatAlarm() {
        viewState.action.value = NavigateToRepeatAlarmSelector(viewState.repeatAlarmType)
    }

    private fun onClickCustomRepeatAlarm() {
        viewState.action.value = NavigateToCustomRepeatAlarmSelector(viewState.selectedDays)
    }

    private fun startEditingTask() = viewModelScope.launch {
        try {
            val task = getTaskUseCase(viewState.taskId).getOrThrow()

            viewState.alarm = task.alarm?.dateTime
            viewState.repeatAlarmType = task.alarm?.repeatType ?: RepeatType.NOT_REPEAT
            viewState.selectedDays = task.alarm?.customDays

            viewState.action.value = TaskFormViewState.Action.SetTaskDetails(
                taskName = task.name,
                taskNotes = task.notes,
            )

            updateAlarm()
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = TaskFormViewState.Action.ShowErrorSavingTask
            viewState.action.value = TaskFormViewState.Action.CloseTaskForm(success = false)
        }
    }

    private fun updateAlarm(animate: Boolean = false) {
        val repeatType = if (viewState.repeatAlarmType == RepeatType.CUSTOM) {
            viewState.selectedDays?.let { alarmService.getCustomRepeatTypeDescription(it) }
        } else {
            alarmService.getRepeatTypeDescription(viewState.repeatAlarmType)
        }

        viewState.alarmForm.value = AlarmForm(
            alarm = viewState.alarm,
            date = viewState.alarm?.let { alarmService.getFullDate(it) },
            time = viewState.alarm?.let { alarmService.getFullTime(it) },
            repeatType = repeatType,
            alarmCustomDays = viewState.selectedDays,
            animate = animate,
            visible = viewState.alarm != null,
        )
    }

    private fun hasTaskToEdit(): Boolean {
        return viewState.taskId != Entity.NO_ID
    }

    private fun hasAlarm(): Boolean {
        return viewState.alarm != null
    }

    private fun initAlarm() {
        viewState.alarm = getInitialAlarm()
        viewState.repeatAlarmType = RepeatType.NOT_REPEAT
    }

    private fun clearAlarm() {
        viewState.repeatAlarmType = RepeatType.NOT_REPEAT
        viewState.alarm = null
        viewState.selectedDays = null
    }

    private fun checkFieldsAndSaveTask(taskName: String, taskNotes: String) =
        viewModelScope.launch {
            try {
                val task = createTaskFromViews(taskName, taskNotes)
                val conditions = checkTaskFieldsUseCase(task)
                conditions.forEach {
                    when (it) {
                        TaskFieldsConditions.TASK_NAME_NOT_FILLED -> {
                            viewState.action.value =
                                TaskFormViewState.Action.ShowErrorTaskNameCantBeEmpty
                        }
                        TaskFieldsConditions.ALARM_NOT_VALID -> {
                            viewState.action.value = TaskFormViewState.Action.ShowErrorAlarmNotValid
                        }
                    }
                    return@launch
                }

                saveTaskUseCase(task)
                viewState.action.value = TaskFormViewState.Action.CloseTaskForm(success = true)
            } catch (e: Exception) {
                logService.error(e)
                viewState.action.value = TaskFormViewState.Action.ShowErrorSavingTask
            }
        }

    private fun createTaskFromViews(taskName: String, taskNotes: String): Task {
        return Task(
            id = viewState.taskId,
            name = taskName,
            notes = taskNotes,
            alarm = createAlarmFromViews()
        )
    }

    private fun createAlarmFromViews(): Alarm? {
        return viewState.alarm?.run {
            Alarm(
                dateTime = this,
                repeatType = viewState.repeatAlarmType,
                customDays = viewState.selectedDays
            )
        }
    }
}
