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
) : BaseViewModel<TaskFormViewState, TaskFormViewIntent>() {
    override fun dispatchViewIntent(viewIntent: TaskFormViewIntent) {
        when (viewIntent) {
            is TaskFormViewIntent.OnStart -> onStart(viewIntent)
            is TaskFormViewIntent.OnClickAddAlarm -> onClickAddAlarm()
            is TaskFormViewIntent.OnClickSaveTask -> onClickSaveTask(viewIntent)
            is TaskFormViewIntent.OnSelectAlarmDate -> onSelectAlarmDate(viewIntent)
            is TaskFormViewIntent.OnSelectAlarmTime -> onSelectAlarmTime(viewIntent)
            is TaskFormViewIntent.OnSelectAlarmType -> onSelectAlarmType(viewIntent)
            is TaskFormViewIntent.OnSelectCustomAlarmType -> onSelectCustomAlarmType(viewIntent)
            is TaskFormViewIntent.OnClickAlarmDate -> onClickAlarmDate()
            is TaskFormViewIntent.OnClickAlarmTime -> onClickAlarmTime()
            is TaskFormViewIntent.OnClickRepeatAlarm -> onClickRepeatAlarm()
            is TaskFormViewIntent.OnClickCustomRepeatAlarm -> onClickCustomRepeatAlarm()
        }
    }

    private fun onStart(viewIntent: TaskFormViewIntent.OnStart) {
        viewState.taskId = viewIntent.taskId

        if (hasTaskToEdit()) {
            viewState.isEditingTask.value = true
            startEditingTask()
        } else {
            viewState.isEditingTask.value = false
            viewState.viewAction.value = TaskFormViewAction.SetFocusOnInputTaskName
            updateAlarm()
        }
    }

    private fun onClickAddAlarm() {
        viewState.viewAction.value = TaskFormViewAction.HideKeyboard

        if (hasAlarm()) {
            clearAlarm()
        } else {
            initAlarm()
        }

        updateAlarm(animate = true)
    }

    private fun onClickSaveTask(viewIntent: TaskFormViewIntent.OnClickSaveTask) {
        try {
            viewState.viewAction.value = TaskFormViewAction.HideKeyboard
            checkFieldsAndSaveTask(viewIntent.taskName, viewIntent.taskNotes)
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskFormViewAction.ShowErrorSavingTask
        }
    }

    private fun onSelectAlarmDate(viewIntent: TaskFormViewIntent.OnSelectAlarmDate) {
        viewState.alarm?.apply {
            set(Calendar.YEAR, viewIntent.year)
            set(Calendar.MONTH, viewIntent.month)
            set(Calendar.DAY_OF_MONTH, viewIntent.day)
        }
        updateAlarm()
    }

    private fun onSelectAlarmTime(viewIntent: TaskFormViewIntent.OnSelectAlarmTime) {
        viewState.alarm?.apply {
            set(Calendar.HOUR_OF_DAY, viewIntent.hourOfDay)
            set(Calendar.MINUTE, viewIntent.minute)
        }
        updateAlarm()
    }

    private fun onSelectAlarmType(viewIntent: TaskFormViewIntent.OnSelectAlarmType) {
        viewState.repeatAlarmType = viewIntent.repeatType
        viewState.selectedDays = null
        updateAlarm()
    }

    private fun onSelectCustomAlarmType(viewIntent: TaskFormViewIntent.OnSelectCustomAlarmType) {
        viewState.selectedDays = null

        when (viewIntent.days.length) {
            1 -> {
                viewState.selectedDays = viewIntent.days
                viewState.repeatAlarmType = RepeatType.CUSTOM

                val dayOfWeek = viewState.alarm?.get(Calendar.DAY_OF_WEEK).toString()
                if (viewIntent.days.contains(dayOfWeek).not()) {
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
                viewState.selectedDays = viewIntent.days
                viewState.repeatAlarmType = RepeatType.CUSTOM

                val dayOfWeek = viewState.alarm?.get(Calendar.DAY_OF_WEEK).toString()
                if (viewIntent.days.contains(dayOfWeek).not()) {
                    val alarm = createAlarmFromViews()
                    viewState.alarm = alarm?.let { getNextAlarmUseCase(it).dateTime }
                }
            }
        }

        updateAlarm()
    }

    private fun onClickAlarmDate() {
        viewState.alarm?.let { alarm ->
            viewState.viewAction.value = TaskFormViewAction.NavigateToAlarmDateSelector(alarm)
        }
    }

    private fun onClickAlarmTime() {
        viewState.alarm?.let { alarm ->
            viewState.viewAction.value = TaskFormViewAction.NavigateToAlarmTimeSelector(alarm)
        }
    }

    private fun onClickRepeatAlarm() {
        viewState.viewAction.value = TaskFormViewAction.NavigateToRepeatAlarmSelector(viewState.repeatAlarmType)
    }

    private fun onClickCustomRepeatAlarm() {
        viewState.viewAction.value = TaskFormViewAction.NavigateToCustomRepeatAlarmSelector(viewState.selectedDays)
    }

    private fun startEditingTask() =
        viewModelScope.launch {
            try {
                val task = getTaskUseCase(viewState.taskId).getOrThrow()

                viewState.alarm = task.alarm?.dateTime
                viewState.repeatAlarmType = task.alarm?.repeatType ?: RepeatType.NOT_REPEAT
                viewState.selectedDays = task.alarm?.customDays

                viewState.viewAction.value =
                    TaskFormViewAction.SetTaskDetails(
                        taskName = task.name,
                        taskNotes = task.notes,
                    )

                updateAlarm()
            } catch (e: Exception) {
                logService.error(e)
                viewState.viewAction.value = TaskFormViewAction.ShowErrorSavingTask
                viewState.viewAction.value = TaskFormViewAction.CloseTaskForm(success = false)
            }
        }

    private fun updateAlarm(animate: Boolean = false) {
        val repeatType =
            if (viewState.repeatAlarmType == RepeatType.CUSTOM) {
                viewState.selectedDays?.let { alarmService.getCustomRepeatTypeDescription(it) }
            } else {
                alarmService.getRepeatTypeDescription(viewState.repeatAlarmType)
            }

        viewState.alarmForm.value =
            AlarmForm(
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

    private fun checkFieldsAndSaveTask(
        taskName: String,
        taskNotes: String,
    ) = viewModelScope.launch {
        try {
            val task = createTaskFromViews(taskName, taskNotes)
            val conditions = checkTaskFieldsUseCase(task)
            conditions.forEach {
                when (it) {
                    TaskFieldsConditions.TASK_NAME_NOT_FILLED -> {
                        viewState.viewAction.value = TaskFormViewAction.ShowErrorTaskNameCantBeEmpty
                    }
                    TaskFieldsConditions.ALARM_NOT_VALID -> {
                        viewState.viewAction.value = TaskFormViewAction.ShowErrorAlarmNotValid
                    }
                }
                return@launch
            }

            saveTaskUseCase(task)
            viewState.viewAction.value = TaskFormViewAction.CloseTaskForm(success = true)
        } catch (e: Exception) {
            logService.error(e)
            viewState.viewAction.value = TaskFormViewAction.ShowErrorSavingTask
        }
    }

    private fun createTaskFromViews(
        taskName: String,
        taskNotes: String,
    ): Task {
        return Task(
            id = viewState.taskId,
            name = taskName,
            notes = taskNotes,
            alarm = createAlarmFromViews(),
        )
    }

    private fun createAlarmFromViews(): Alarm? {
        return viewState.alarm?.run {
            Alarm(
                dateTime = this,
                repeatType = viewState.repeatAlarmType,
                customDays = viewState.selectedDays,
            )
        }
    }
}
