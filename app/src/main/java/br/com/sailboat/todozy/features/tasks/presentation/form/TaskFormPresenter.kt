package br.com.sailboat.todozy.features.tasks.presentation.form

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.exceptions.RequiredFieldNotFilledException
import br.com.sailboat.todozy.core.extensions.getInitialAlarm
import br.com.sailboat.todozy.core.extensions.incrementToNextValidDate
import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.usecase.CheckTaskFields
import br.com.sailboat.todozy.features.tasks.domain.usecase.CheckTaskFields.Condition.ALARM_NOT_VALID
import br.com.sailboat.todozy.features.tasks.domain.usecase.CheckTaskFields.Condition.TASK_NAME_NOT_FILLED
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTask
import br.com.sailboat.todozy.features.tasks.domain.usecase.SaveTask
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetNextAlarm
import kotlinx.coroutines.runBlocking
import java.util.*

class TaskFormPresenter(private val getTask: GetTask,
                        private val saveTask: SaveTask,
                        private val getNextAlarm: GetNextAlarm) :
        BasePresenter<TaskFormContract.View>(), TaskFormContract.Presenter {

    private val viewModel by lazy { TaskFormViewModel() }

    override fun onStart() {
        extractTaskId()

        if (hasTaskToEdit()) {
            startEditingTask()
        } else {
            updateViews()
            view?.setFocusOnInputTaskName()
        }
    }

    override fun onRestart() {
        updateViews()
    }

    override fun onClickAddAlarm() {
        view?.hideKeyboard()

        if (hasAlarm()) {
            clearAlarm()
        } else {
            initAlarm()
        }

        updateAlarmViewsWithAnimation()
    }

    override fun onClickAlarmTime() {
        view?.hideKeyboard()
        viewModel.alarm?.run { view?.showAlarmTimePickerDialog(this) }
    }

    override fun onClickSaveTask() {
        try {
            view?.hideKeyboard()
            extractInfoFromViews()
            checkFieldsAndSaveTask()

        } catch (e: RequiredFieldNotFilledException) {
            view?.showSimpleMessage(e.message!!)

        } catch (e: Exception) {
            view?.log(e)
            view?.showErrorOnSave()
        }
    }


    override fun onAlarmDateSelected(year: Int, month: Int, day: Int) {
        viewModel.alarm?.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        updateAlarmViews()
    }

    override fun onAlarmTimeSelected(hourOfDay: Int, minute: Int) {
        viewModel.alarm?.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        updateAlarmViews()
    }

    override fun onClickRepeatAlarm() {
        view?.hideKeyboard()
        view?.showRepeatAlarmOptions(viewModel.repeatAlarmType)
    }

    override fun onClickItemCustomRepeatAlarm() {
        view?.showWeekDaysSelector(viewModel.selectedDays)
    }

    override fun onSelectAlarmType(repeatAlarmItem: RepeatType) {
        viewModel.repeatAlarmType = repeatAlarmItem
        viewModel.selectedDays = null
        updateAlarmRepeatTypeView()
    }

    override fun onClickAlarmDate() {
        view?.hideKeyboard()
        view?.showAlarmDatePickerDialog(viewModel.alarm!!)
    }

    override fun onSelectRepeatAlarmCustom(days: String) {
        viewModel.selectedDays = null

        if (days.length == 1) {

            if (!days.contains(viewModel.alarm?.get(Calendar.DAY_OF_WEEK).toString())) {
                // TODO: ADD GET NEXT ALARM USECASE
                viewModel.alarm?.incrementToNextValidDate(RepeatType.CUSTOM, days)
            }

            viewModel.repeatAlarmType = RepeatType.WEEK
        } else if (days.length == 7) {
            viewModel.repeatAlarmType = RepeatType.DAY
        } else {

            if (!days.contains(viewModel.alarm?.get(Calendar.DAY_OF_WEEK).toString())) {
                viewModel.alarm?.incrementToNextValidDate(RepeatType.CUSTOM, days)
            }

            viewModel.repeatAlarmType = RepeatType.CUSTOM
            viewModel.selectedDays = days
        }

        updateViews()
    }

    private fun startEditingTask() = launchMain {
        try {
            view?.showProgress()
            val taskId = viewModel.taskId

            val task = getTask(taskId)
            viewModel.name = task.name
            viewModel.notes = task.notes ?: ""

            task.alarm?.run {
                viewModel.alarm = dateTime
                viewModel.repeatAlarmType = repeatType
                viewModel.selectedDays = customDays
            }

            view?.hideProgress()
            view?.setScreenToHideKeyboard()
            updateName()
            updateNotes()
            updateViews()

        } catch (e: Exception) {
            view?.hideProgress()
            view?.log(e)
            view?.closeWithResultNotOk()
        }

    }

    private fun updateViews() {
        updateTitle()
        updateAlarmViews()
    }

    private fun updateTitle() = view?.run {
        if (hasTaskToEdit()) showEditTaskTitle() else showNewTaskTitle()
    }

    private fun updateAlarmViews() {
        if (hasAlarm()) {
            view?.showAlarm()
            view?.setAlarmDate(viewModel.alarm!!)
            view?.setAlarmTime(viewModel.alarm!!)
            updateAlarmRepeatTypeView()
        } else {
            view?.hideAlarm()
        }
    }

    private fun updateAlarmViewsWithAnimation() {
        if (hasAlarm()) {
            view?.showAlarmWithAnimation()
            view?.setAlarmDate(viewModel.alarm!!)
            view?.setAlarmTime(viewModel.alarm!!)
            updateAlarmRepeatTypeView()
        } else {
            view?.hideAlarmWithAnimation()
        }
    }

    private fun updateAlarmRepeatTypeView() {
        if (viewModel.repeatAlarmType == RepeatType.CUSTOM) {
            view?.setCustomRepeatType(viewModel.selectedDays)
        } else {
            view?.setRepeatType(viewModel.repeatAlarmType)
        }
    }

    private fun checkFieldsAndSaveTask() = runBlocking {
        try {
            val task = getTaskFromViews()
            val conditions = CheckTaskFields().invoke(task)
            conditions.forEach {
                when (it) {
                    TASK_NAME_NOT_FILLED -> view?.showErrorTaskNameCantBeBlank()
                    ALARM_NOT_VALID -> view?.showErrorAlarmNotValid()
                }
                return@runBlocking
            }

            saveTask(task)
            view?.closeWithResultOk()

        } catch (e: Exception) {
            view?.log(e)
            // TODO: Show error
            view?.showSimpleMessage("Error on save Task!!!!!")
        }

    }

    private fun getTaskFromViews(): Task {
        val alarm: Alarm? = viewModel.alarm?.run {
            Alarm(dateTime = this, repeatType = viewModel.repeatAlarmType, customDays = viewModel.selectedDays)
        }

        return Task(id = viewModel.taskId,
                name = viewModel.name,
                notes = viewModel.notes,
                alarm = alarm)
    }

    private fun extractInfoFromViews() {
        viewModel.name = view?.getName() ?: ""
        viewModel.notes = view?.getNotes() ?: ""
    }

    private fun initAlarm() {
        viewModel.alarm = getInitialAlarm()
        viewModel.repeatAlarmType = RepeatType.NOT_REPEAT
    }

    private fun clearAlarm() {
        viewModel.alarm = null
        viewModel.repeatAlarmType = RepeatType.NOT_REPEAT
        viewModel.selectedDays = null
    }

    private fun hasAlarm() = (viewModel.alarm != null)

    private fun hasTaskToEdit() = (viewModel.taskId != Entity.NO_ID)

    private fun updateName() = view?.setName(viewModel.name)

    private fun updateNotes() = view?.setNotes(viewModel.notes)

    private fun extractTaskId() {
        viewModel.taskId = view?.getTaskId() ?: Entity.NO_ID
    }

}