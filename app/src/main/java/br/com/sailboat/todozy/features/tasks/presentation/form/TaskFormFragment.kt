package br.com.sailboat.todozy.features.tasks.presentation.form

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.extensions.hideKeyboard
import br.com.sailboat.todozy.core.extensions.setActivityToHideKeyboard
import br.com.sailboat.todozy.core.extensions.showKeyboard
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.dialog.DateSelectorDialog
import br.com.sailboat.todozy.core.presentation.dialog.TimeSelectorDialog
import br.com.sailboat.todozy.core.presentation.dialog.selectable.RepeatAlarmSelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.selectable.SelectItemDialog
import br.com.sailboat.todozy.core.presentation.dialog.selectable.SelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.weekdays.WeekDaysSelectorDialog
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.databinding.FrgTaskFormBinding
import br.com.sailboat.todozy.databinding.FrgTaskHistoryBinding
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import org.koin.android.ext.android.inject
import java.util.*


class TaskFormFragment : BaseMVPFragment<TaskFormContract.Presenter>(), TaskFormContract.View {

    override val presenter: TaskFormContract.Presenter by inject()

    companion object {
        fun newInstance() = TaskFormFragment()

        fun newInstance(taskId: Long): TaskFormFragment = with(TaskFormFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }

    }

    private lateinit var binding: FrgTaskFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgTaskFormBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun initViews() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarTaskForm)
        binding.toolbarTaskForm.setNavigationIcon(R.drawable.ic_close_white_24dp)
        binding.toolbarTaskForm.setNavigationOnClickListener { activity?.onBackPressed() }

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

    override fun getTaskId(): Long {
        return arguments?.getTaskId() ?: Entity.NO_ID
    }

    override fun showNewTaskTitle() = binding.toolbarTaskForm.setTitle(R.string.new_task)

    override fun showEditTaskTitle() = binding.toolbarTaskForm.setTitle(R.string.edit_task)

    override fun showAlarmDatePickerDialog(alarm: Calendar) {
        DateSelectorDialog.show(childFragmentManager, alarm, object : DateSelectorDialog.Callback {
            override fun onDateSet(year: Int, month: Int, day: Int) {
                presenter.onAlarmDateSelected(year, month, day)
            }
        })
    }

    override fun showAlarmTimePickerDialog(alarm: Calendar) {
        TimeSelectorDialog.show(childFragmentManager, alarm, object : TimeSelectorDialog.Callback {
            override fun onTimeSet(hourOfDay: Int, minute: Int) {
                presenter.onAlarmTimeSelected(hourOfDay, minute)
            }
        })
    }

    override fun showRepeatAlarmOptions(repeatAlarmItem: RepeatType) {
        SelectItemDialog.show(childFragmentManager,
            getString(R.string.repeat_alarm),
            RepeatAlarmSelectableItem.getItems(),
            RepeatAlarmSelectableItem.getFromId(repeatAlarmItem.ordinal),
            object : SelectItemDialog.Callback {
                override fun onClickItem(item: SelectableItem) = onSelectItemRepeatAlarm(item)
            })
    }

    override fun showWeekDaysSelector(days: String?) {
        WeekDaysSelectorDialog.show(
            childFragmentManager,
            days,
            object : WeekDaysSelectorDialog.Callback {
                override fun onClickOk(selectedDays: String) {
                    if (selectedDays.isNotEmpty()) {
                        presenter.onSelectRepeatAlarmCustom(selectedDays)
                    } else {
                        presenter.onSelectAlarmType(RepeatType.NOT_REPEAT)
                    }
                }
            })
    }

    override fun setName(name: String) = with(binding) {
        etTaskFormName.setText(name)
        etTaskFormName.setSelection(etTaskFormName.length())
    }

    override fun setNotes(notes: String) {
        binding.etTaskFormNotes.setText(notes)
    }

    override fun showAlarm() {
        binding.alarmDetails.llAlarmOptions.visible()
    }

    override fun showAlarmWithAnimation() {
        AnimationHelper().expand(binding.alarmDetails.llAlarmOptions)
    }

    override fun hideAlarm() {
        binding.alarmDetails.llAlarmOptions.gone()
    }

    override fun hideAlarmWithAnimation() {
        AnimationHelper().collapse(binding.alarmDetails.llAlarmOptions)
    }

    override fun setAlarmDate(date: Calendar) {
        activity?.run { binding.alarmDetails.tvAlarmDate.text = date.getFullDateName(this) }
    }

    override fun setAlarmTime(time: Calendar) {
        activity?.run {
            binding.alarmDetails.tvAlarmTime.text = time.formatTimeWithAndroidFormat(this)
        }
    }

    override fun setRepeatType(repeatType: RepeatType) {
        binding.alarmDetails.tvAlarmRepeat.setText(RepeatTypeView.getFromRepeatType(repeatType).description)
    }

    override fun getName(): String {
        return binding.etTaskFormName.text.toString()
    }

    override fun getNotes(): String {
        return binding.etTaskFormNotes.text.toString()
    }

    override fun showErrorOnSave() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setScreenToHideKeyboard() {
        activity?.run { setActivityToHideKeyboard() }
    }

    override fun setCustomRepeatType(custom: String?) {
        activity?.run {
            binding.alarmDetails.tvAlarmRepeat.text = WeekDaysHelper().getCustomRepeat(this, custom)
        }
    }

    override fun setFocusOnInputTaskName() {
        binding.etTaskFormName.requestFocus()
        activity?.showKeyboard(binding.etTaskFormName)
    }

    override fun showErrorTaskNameCantBeBlank() {
        binding.etTaskFormName.error = getString(R.string.exception_task_name)
    }

    override fun showErrorAlarmNotValid() {
        Toast.makeText(activity, "Alarm not valid", Toast.LENGTH_SHORT).show()
    }

    private fun initAlarmViews() {
        binding.rlTaskFormAddAlarm.setOnClickListener { presenter.onClickAddAlarm() }
        binding.alarmDetails.tvAlarmDate.setOnClickListener { presenter.onClickAlarmDate() }
        binding.alarmDetails.tvAlarmTime.setOnClickListener { presenter.onClickAlarmTime() }
        binding.alarmDetails.tvAlarmRepeat.setOnClickListener { presenter.onClickRepeatAlarm() }
    }

    private fun initEditTexts() {
        binding.etTaskFormName.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                activity?.hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun onSelectItemRepeatAlarm(item: SelectableItem) = with(presenter) {
        when (item as RepeatAlarmSelectableItem) {
            RepeatAlarmSelectableItem.NOT_REPEAT -> onSelectAlarmType(RepeatType.NOT_REPEAT)
            RepeatAlarmSelectableItem.DAY -> onSelectAlarmType(RepeatType.DAY)
            RepeatAlarmSelectableItem.WEEK -> onSelectAlarmType(RepeatType.WEEK)
            RepeatAlarmSelectableItem.MONTH -> onSelectAlarmType(RepeatType.MONTH)
            RepeatAlarmSelectableItem.YEAR -> onSelectAlarmType(RepeatType.YEAR)
            RepeatAlarmSelectableItem.CUSTOM -> onClickItemCustomRepeatAlarm()
        }
    }

}
