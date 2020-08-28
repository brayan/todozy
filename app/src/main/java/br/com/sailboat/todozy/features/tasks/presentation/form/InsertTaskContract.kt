package br.com.sailboat.todozy.features.tasks.presentation.form

import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPContract
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import java.util.*

interface InsertTaskContract {

    interface View : BaseMVPContract.View {
        fun showAlarmDatePickerDialog(alarm: Calendar)
        fun showAlarmTimePickerDialog(alarm: Calendar)
        fun showRepeatAlarmOptions(repeatAlarmItem: RepeatType)
        fun setName(name: String)
        fun setNotes(notes: String)
        fun showAlarm()
        fun showAlarmWithAnimation()
        fun hideAlarm()
        fun hideAlarmWithAnimation()
        fun setAlarmDate(date: Calendar)
        fun setAlarmTime(time: Calendar)
        fun setRepeatType(repeatType: RepeatType)
        fun getName(): String
        fun getNotes(): String
        fun showWeekDaysSelector(selectedDays: String?)
        fun getTaskId(): Long
        fun showNewTaskTitle()
        fun showEditTaskTitle()
        fun showErrorAlarmNotValid()
        fun showErrorOnSave()
        fun showErrorTaskNameCantBeBlank()
        fun setScreenToHideKeyboard()
        fun setCustomRepeatType(custom: String?)
    }

    interface Presenter : BaseMVPContract.Presenter {
        fun onAlarmDateSelected(year: Int, month: Int, day: Int)
        fun onSelectRepeatAlarmCustom(days: String)
        fun onClickSaveTask()
        fun onAlarmTimeSelected(hourOfDay: Int, minute: Int)
        fun onSelectAlarmType(alarmType: RepeatType)
        fun onClickAddAlarm()
        fun onClickAlarmDate()
        fun onClickAlarmTime()
        fun onClickRepeatAlarm()
        fun onClickItemCustomRepeatAlarm()
    }

}