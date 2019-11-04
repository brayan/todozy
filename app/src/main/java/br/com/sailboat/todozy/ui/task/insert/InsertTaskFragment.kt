package br.com.sailboat.todozy.ui.task.insert

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPFragment
import br.com.sailboat.todozy.ui.dialog.DateSelectorDialog
import br.com.sailboat.todozy.ui.dialog.TimeSelectorDialog
import br.com.sailboat.todozy.ui.dialog.selectable.RepeatAlarmSelectableItem
import br.com.sailboat.todozy.ui.dialog.selectable.SelectableItem
import br.com.sailboat.todozy.ui.dialog.week_days.WeekDaysSelectorDialog
import br.com.sailboat.todozy.ui.helper.*
import kotlinx.android.synthetic.main.alarm_insert.*
import kotlinx.android.synthetic.main.frg_insert_task.*
import kotlinx.android.synthetic.main.frg_task_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import org.koin.android.ext.android.inject
import java.util.*

class InsertTaskFragment : BaseMVPFragment<InsertTaskContract.Presenter>(), InsertTaskContract.View, View.OnClickListener {

    override val presenter: InsertTaskContract.Presenter by inject()
    override val layoutId = R.layout.frg_insert_task

    companion object {
        fun newInstance() = InsertTaskFragment()

        fun newInstance(taskId: Long): InsertTaskFragment = with(InsertTaskFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }

    }

    override fun initViews() {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        initEditTexts()
        initAlarmViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_insert, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> presenter.onClickSaveTask()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun getTaskToEdit(): Long {
        return arguments?.getTaskId() ?: EntityHelper.NO_ID
    }

    override fun showNewTaskTitle() = toolbar.setTitle(R.string.new_task)

    override fun showEditTaskTitle() = toolbar.setTitle(R.string.edit_task)

    override fun showAlarmDatePickerDialog(alarm: Calendar) {
        DateSelectorDialog.show(fragmentManager!!, alarm, object: DateSelectorDialog.Callback {
            override fun onDateSet(year: Int, month: Int, day: Int) {
                presenter.onAlarmDateSelected(year, month, day)
            }

        })
    }

    override fun showAlarmTimePickerDialog(alarm: Calendar) {
        TimeSelectorDialog.show(fragmentManager!!, alarm, object: TimeSelectorDialog.Callback {
            override fun onTimeSet(hourOfDay: Int, minute: Int) {
                presenter.onAlarmTimeSelected(hourOfDay, minute)
            }

        })
    }

    override fun showRepeatAlarmOptions(repeatAlarmItem: RepeatType?) {
//        SelectItemDialog.show(fragmentManager, getString(R.string.repeat_alarm), RepeatAlarmSelectableItem.getItems(),
//                RepeatTypeView.getFromRepeatType(repeatAlarmItem), SelectItemDialog.Callback { item -> onClickItemRepeatAlarm(item) })
    }

    override fun showWeekDaysSelector(days: String?) {
        WeekDaysSelectorDialog.show(fragmentManager!!, days, object: WeekDaysSelectorDialog.Callback {
            override fun onClickOk(selectedDays: String) {
                if (selectedDays.isNotEmpty()) {
                    presenter.onSelectRepeatAlarmCustom(selectedDays)
                } else {
                    presenter.onSelectAlarmType(RepeatType.NOT_REPEAT)
                }
            }

        })
    }

    override fun setName(name: String) {
        frag_insert_task__et__name.setText(name)
        frag_insert_task__et__name.setSelection(frag_insert_task__et__name.length())
    }

    override fun setNotes(notes: String) {
        frag_insert_task__et__notes!!.setText(notes)
    }

    override fun showAlarm() {
        alarm__ll__options.visibility = View.VISIBLE
    }

    override fun showAlarmWithAnimation() {
        AnimationHelper().expand(alarm__ll__options)
    }

    override fun hideAlarm() {
        alarm__ll__options.gone()
    }

    override fun hideAlarmWithAnimation() {
        AnimationHelper().collapse(alarm__ll__options)
    }

    override fun setAlarmDate(date: Calendar) {
        activity?.run { alarm__tv__date.text = date.getFullDateName(this) }
    }

    override fun setAlarmTime(time: Calendar) {
        activity?.run { alarm__tv__time.text = time.formatTimeWithAndroidFormat(this) }
    }

    override fun setRepeatType(repeatType: RepeatType) {
        alarm__tv__repeat.setText(RepeatTypeView.getFromRepeatType(repeatType).description)
    }

    override fun getName(): String {
        return frag_insert_task__et__name.text.toString()
    }

    override fun getNotes(): String {
        return frag_insert_task__et__notes.text.toString()
    }

    override fun showErrorOnSave() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.frag_insert_task__rl__add_alarm -> {
                presenter.onClickAddAlarm()
                return
            }
            R.id.alarm__tv__date -> {
                presenter.onClickAlarmDate()
                return
            }
            R.id.alarm__tv__time -> {
                presenter.onClickAlarmTime()
                return
            }
            R.id.alarm__tv__repeat -> {
                presenter.onClickRepeatAlarm()
                return
            }
        }
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setScreenToHideKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCustomRepeatType(custom: String) {
        activity?.run { WeekDaysHelper().getCustomRepeat(this, custom) }
    }

    override fun showErrorTaskNameCantBeBlank() {
        frag_insert_task__et__name.error = getString(R.string.exception_task_name)
    }

    override fun showErrorAlarmNotValid() {
        Toast.makeText(activity, "Alarm not valid", Toast.LENGTH_SHORT).show()
    }

    private fun initAlarmViews() {
        frag_insert_task__rl__add_alarm.setOnClickListener(this)
        alarm__tv__date.setOnClickListener(this)
        alarm__tv__time.setOnClickListener(this)
        alarm__tv__repeat.setOnClickListener(this)
    }

    private fun initEditTexts() {

        frag_insert_task__et__name.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                activity?.hideKeyboard()
                return@OnKeyListener true
            }

            false
        })
    }

    private fun onClickItemRepeatAlarm(item: SelectableItem) {
        when (item as RepeatAlarmSelectableItem) {
            RepeatAlarmSelectableItem.NOT_REPEAT -> {
                presenter.onSelectAlarmType(RepeatType.NOT_REPEAT)
                return
            }
            RepeatAlarmSelectableItem.DAY -> {
                presenter.onSelectAlarmType(RepeatType.DAY)
                return
            }
            RepeatAlarmSelectableItem.WEEK -> {
                presenter.onSelectAlarmType(RepeatType.WEEK)
                return
            }
            RepeatAlarmSelectableItem.MONTH -> {
                presenter.onSelectAlarmType(RepeatType.MONTH)
                return
            }
            RepeatAlarmSelectableItem.YEAR -> {
                presenter.onSelectAlarmType(RepeatType.YEAR)
                return
            }
            RepeatAlarmSelectableItem.CUSTOM -> {
                presenter.onClickItemCustomRepeatAlarm()
                return
            }
        }
    }

}
