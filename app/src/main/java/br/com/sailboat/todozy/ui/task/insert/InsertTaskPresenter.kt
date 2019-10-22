package br.com.sailboat.todozy.ui.task.insert

import br.com.sailboat.todozy.domain.exceptions.RequiredFieldNotFilledException
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.getInitialAlarm
import br.com.sailboat.todozy.domain.helper.incrementToNextValidDate
import br.com.sailboat.todozy.domain.helper.isBeforeNow
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.tasks.GetTask
import br.com.sailboat.todozy.domain.tasks.SaveTask
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.helper.RepeatTypeView
import java.util.*

class InsertTaskPresenter(private val getTask: GetTask,
                          private val saveTask: SaveTask) :
        BasePresenter<InsertTaskContract.View>(), InsertTaskContract.Presenter {

    private val viewModel by lazy { InsertTaskViewModel() }

    override fun onStart() {
        extractTaskId()

        if (hasTaskToEdit()) {
            startEditingTask()
        } else {
            updateViews()
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

    override fun onClickMenuSave() {
        try {
            view?.hideKeyboard()
            extractInfoFromViews()
            performValidations()
            saveRecords()

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

    override fun onSelectRepeatAlarmCustom(selectedDays: String) {
        viewModel.selectedDays = null

        if (selectedDays.length == 1) {

            if (!selectedDays.contains(viewModel.alarm?.get(Calendar.DAY_OF_WEEK).toString())) {
                viewModel.alarm?.incrementToNextValidDate(RepeatType.CUSTOM, selectedDays)
            }

            viewModel.repeatAlarmType = RepeatType.WEEK
        } else if (selectedDays.length == 7) {
            viewModel.repeatAlarmType = RepeatType.DAY
        } else {

            if (!selectedDays.contains(viewModel.alarm?.get(Calendar.DAY_OF_WEEK).toString())) {
                viewModel.alarm?.incrementToNextValidDate(RepeatType.CUSTOM, selectedDays)
            }

            viewModel.repeatAlarmType = RepeatType.CUSTOM
            viewModel.selectedDays = selectedDays
        }

        updateViews()
    }

    private fun startEditingTask() = launchAsync {
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

            view?.closeProgress()
            view?.setScreenToHideKeyboard()
            updateName()
            updateNotes()
            updateViews()

        } catch (e: Exception) {
            view?.closeProgress()
            view?.log(e)
            view?.closeWithResultNotOk()
        }

    }

    private fun updateViews() {
        updateTitle()
        updateAlarmViews()
    }

    private fun updateTitle() {
        if (hasTaskToEdit()) {
            view?.showEditTaskTitle()
        } else {
            view?.showNewTaskTitle()
        }
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
            view?.setCustomRepeatType(viewModel.selectedDays!!)
        } else {
            view?.setRepeatType(viewModel.repeatAlarmType!!)
        }
    }

    private fun saveRecords() = launchAsync {
        try {
            val task = getTaskFromViews()
            saveTask(task)

//            AlarmManagerHelper.cancelAlarm(getContext(), task)
//            AlarmSQLite.newInstance(getContext()).deleteByTask(task.getId())
//
//            if (viewModel.alarm != null) {
//                alarm = Alarm()
//                alarm.setDays(viewModel.selectedDays)
//                alarm.setNextAlarmDate(parseCalendarToString(viewModel.alarm))
//                alarm.setRepeatType(viewModel.repeatAlarmItem.id)
//                alarm.setInsertingDate(parseCalendarToString(Calendar.getInstance()))
//                alarm.setTaskId(task.getId())
//                alarm.setId(viewModel.alarmId)
//
//                AlarmSQLite.newInstance(getContext()).save(alarm)
//
//                AlarmManagerHelper.setNextValidAlarm(getContext(), task, alarm)
//            }
//
//
//            closeActivityWithResultOk()


        } catch (e: Exception) {
            view?.log(e)
            // TODO: Show error
        }

    }


    private fun getTaskFromViews(): Task {
        val alarm: Alarm? = viewModel.alarm?.run {
            Alarm(dateTime = this, repeatType = viewModel.repeatAlarmType!!)
        }

        val task = Task(id = viewModel.taskId,
                name = viewModel.name,
                notes = viewModel.notes,
                alarm = alarm)

        return task
    }

    private fun extractInfoFromViews() {
        viewModel.name = view?.getName() ?: ""
        viewModel.notes = view?.getNotes() ?: ""
    }

    @Throws(Exception::class)
    private fun performValidations() {
        validateName()
        validarAlarm()
    }

    @Throws(Exception::class)
    private fun validateName() {
        val name = viewModel.name

        if (name.isEmpty()) {
            //throw RequiredFieldNotFilledException(getString(R.string.exception_task_name))
        }
    }

    @Throws(Exception::class)
    private fun validarAlarm() {
        val alarm = viewModel.alarm

        if (alarm?.isBeforeNow() == true) {
            //throw RequiredFieldNotFilledException(getString(R.string.msg_invalid_date_time))
        }

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

    private fun hasTaskToEdit() = (viewModel.taskId != EntityHelper.NO_ID)

    private fun updateName() = view?.setName(viewModel.name)

    private fun updateNotes() = view?.setNotes(viewModel.notes)

    private fun extractTaskId() {
        viewModel.taskId = view?.getTaskToEdit() ?: EntityHelper.NO_ID
    }

}