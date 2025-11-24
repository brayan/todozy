package br.com.sailboat.todozy.feature.task.form.impl.presentation

import android.app.Activity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.databinding.FragmentTaskFormBinding
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnClickAddAlarm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnClickAlarmDate
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnClickAlarmTime
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnClickCustomRepeatAlarm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnClickRepeatAlarm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnClickSaveTask
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnSelectAlarmDate
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnSelectAlarmTime
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnSelectAlarmType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnSelectCustomAlarmType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewIntent.OnStart
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewModel
import br.com.sailboat.todozy.utility.android.activity.hideKeyboard
import br.com.sailboat.todozy.utility.android.activity.showKeyboard
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.dialog.selectable.SelectItemDialog
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem
import br.com.sailboat.uicomponent.impl.dialog.weekdays.WeekDaysSelectorDialog
import br.com.sailboat.uicomponent.impl.helper.AnimationHelper
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.putTaskId
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import java.util.TimeZone
import br.com.sailboat.uicomponent.impl.R as UiR
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TaskFormFragment : Fragment() {

    private val viewModel: TaskFormViewModel by viewModel()

    private lateinit var binding: FragmentTaskFormBinding

    companion object {
        private const val DATE_PICKER_TAG = "TASK_FORM_DATE_PICKER"
        private const val TIME_PICKER_TAG = "TASK_FORM_TIME_PICKER"

        fun newInstance() = TaskFormFragment()

        fun newInstance(taskId: Long): TaskFormFragment = with(TaskFormFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTaskFormBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        reattachMaterialPickers()

        val taskId = arguments?.getTaskId() ?: Entity.NO_ID
        viewModel.dispatchViewIntent(OnStart(taskId))
    }

    private fun initViews() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarTaskForm)
        binding.toolbarTaskForm.setNavigationIcon(UiR.drawable.ic_close_white_24dp)
        binding.toolbarTaskForm.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        addMenuProvider()

        initEditTexts()
        initAlarmViews()
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.alarmForm.observe(viewLifecycleOwner) { alarmForm ->
            binding.alarmDetails.tvAlarmDate.text = alarmForm.date
            binding.alarmDetails.tvAlarmTime.text = alarmForm.time
            binding.alarmDetails.tvAlarmRepeat.text = alarmForm.repeatType

            updateAlarmVisibility(alarmForm.visible, alarmForm.animate)
        }

        viewModel.viewState.isEditingTask.observe(viewLifecycleOwner) { isEditingTask ->
            if (isEditingTask) {
                binding.toolbarTaskForm.setTitle(UiR.string.edit_task)
            } else {
                binding.toolbarTaskForm.setTitle(UiR.string.new_task)
            }
        }
    }

    private fun observeActions() {
        viewModel.viewState.viewAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is TaskFormViewAction.CloseTaskForm -> closeTaskForm(action)
                is TaskFormViewAction.SetFocusOnInputTaskName -> setFocusOnInputTaskName()
                is TaskFormViewAction.SetTaskDetails -> setTaskDetails(action)
                is TaskFormViewAction.ShowErrorSavingTask -> showErrorSavingTask()
                is TaskFormViewAction.HideKeyboard -> hideKeyboard()
                is TaskFormViewAction.ShowErrorAlarmNotValid -> showErrorAlarmNotValid()
                is TaskFormViewAction.ShowErrorTaskNameCantBeEmpty -> showErrorTaskNameCantBeBlank()
                is TaskFormViewAction.NavigateToAlarmDateSelector -> navigateToAlarmDateSelector(action)
                is TaskFormViewAction.NavigateToAlarmTimeSelector -> navigateToAlarmTimeSelector(action)
                is TaskFormViewAction.NavigateToRepeatAlarmSelector -> navigateToRepeatAlarmSelector(action)
                is TaskFormViewAction.NavigateToCustomRepeatAlarmSelector -> navigateToCustomRepeatAlarmSelector(action)
            }
        }
    }

    private fun closeTaskForm(action: TaskFormViewAction.CloseTaskForm) {
        val result = if (action.success) {
            Activity.RESULT_OK
        } else {
            Activity.RESULT_CANCELED
        }
        activity?.setResult(result)
        activity?.finish()
    }

    private fun setFocusOnInputTaskName() {
        binding.etTaskFormName.requestFocus()
        activity?.showKeyboard(binding.etTaskFormName)
    }

    private fun setTaskDetails(action: TaskFormViewAction.SetTaskDetails) = with(binding) {
        etTaskFormNotes.setText(action.taskNotes)

        etTaskFormName.setText(action.taskName)
        etTaskFormName.setSelection(etTaskFormName.length())
    }

    private fun showErrorSavingTask() {
        Toast.makeText(activity, UiR.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    private fun addMenuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(UiR.menu.menu_insert, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    UiR.id.menu_save -> {
                        val action = OnClickSaveTask(
                            taskName = binding.etTaskFormName.text.toString(),
                            taskNotes = binding.etTaskFormNotes.text.toString(),
                        )
                        viewModel.dispatchViewIntent(action)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun navigateToAlarmDateSelector(action: TaskFormViewAction.NavigateToAlarmDateSelector) {
        if (childFragmentManager.findFragmentByTag(DATE_PICKER_TAG) != null) return

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(UiR.string.label_date))
            .setSelection(action.currentDate.timeInMillis)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            handleDateSelection(selection)
        }

        picker.show(childFragmentManager, DATE_PICKER_TAG)
    }

    private fun navigateToAlarmTimeSelector(action: TaskFormViewAction.NavigateToAlarmTimeSelector) {
        if (childFragmentManager.findFragmentByTag(TIME_PICKER_TAG) != null) return

        val picker = MaterialTimePicker.Builder()
            .setTitleText(UiR.string.label_time)
            .setTimeFormat(
                if (DateFormat.is24HourFormat(requireContext())) {
                    TimeFormat.CLOCK_24H
                } else {
                    TimeFormat.CLOCK_12H
                }
            )
            .setHour(action.currentTime.get(Calendar.HOUR_OF_DAY))
            .setMinute(action.currentTime.get(Calendar.MINUTE))
            .build()

        picker.addOnPositiveButtonClickListener {
            handleTimeSelection(picker.hour, picker.minute)
        }

        picker.show(childFragmentManager, TIME_PICKER_TAG)
    }

    private fun navigateToRepeatAlarmSelector(action: TaskFormViewAction.NavigateToRepeatAlarmSelector) {
        SelectItemDialog.show(
            "TAGZERA",
            childFragmentManager,
            getString(UiR.string.repeat_alarm),
            RepeatAlarmSelectableItem.getItems(),
            RepeatAlarmSelectableItem.getFromId(action.repeatType.ordinal),
            object : SelectItemDialog.Callback {
                override fun onClickItem(item: SelectableItem) = onSelectItemRepeatAlarm(item)
            }
        )
    }

    private fun navigateToCustomRepeatAlarmSelector(action: TaskFormViewAction.NavigateToCustomRepeatAlarmSelector) {
        WeekDaysSelectorDialog.show(
            childFragmentManager,
            action.days,
            object : WeekDaysSelectorDialog.Callback {
                override fun onClickOk(selectedDays: String) {
                    if (selectedDays.isNotEmpty()) {
                        viewModel.dispatchViewIntent(OnSelectCustomAlarmType(selectedDays))
                    } else {
                        viewModel.dispatchViewIntent(OnSelectAlarmType(RepeatType.NOT_REPEAT))
                    }
                }
            }
        )
    }

    private fun reattachMaterialPickers() {
        (childFragmentManager.findFragmentByTag(DATE_PICKER_TAG) as? MaterialDatePicker<*>)?.let { picker ->
            picker.clearOnPositiveButtonClickListeners()
            picker.addOnPositiveButtonClickListener { selection ->
                handleDateSelection(selection)
            }
        }

        (childFragmentManager.findFragmentByTag(TIME_PICKER_TAG) as? MaterialTimePicker)?.let { picker ->
            picker.clearOnPositiveButtonClickListeners()
            picker.addOnPositiveButtonClickListener {
                handleTimeSelection(picker.hour, picker.minute)
            }
        }
    }

    private fun handleDateSelection(selection: Any?) {
        val millis = selection as? Long ?: return
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = millis
        }
        viewModel.dispatchViewIntent(
            OnSelectAlarmDate(
                year = utcCalendar.get(Calendar.YEAR),
                month = utcCalendar.get(Calendar.MONTH),
                day = utcCalendar.get(Calendar.DAY_OF_MONTH),
            )
        )
    }

    private fun handleTimeSelection(hourOfDay: Int, minute: Int) {
        viewModel.dispatchViewIntent(
            OnSelectAlarmTime(
                hourOfDay = hourOfDay,
                minute = minute,
            )
        )
    }

    private fun showAlarm() {
        binding.alarmDetails.llAlarmOptions.visible()
    }

    private fun showAlarmWithAnimation() {
        AnimationHelper().expand(binding.alarmDetails.llAlarmOptions)
    }

    private fun hideAlarm() {
        binding.alarmDetails.llAlarmOptions.gone()
    }

    private fun hideAlarmWithAnimation() {
        AnimationHelper().collapse(binding.alarmDetails.llAlarmOptions)
    }

    private fun showErrorTaskNameCantBeBlank() {
        binding.etTaskFormName.error = getString(UiR.string.exception_task_name)
    }

    private fun showErrorAlarmNotValid() {
        Toast.makeText(activity, getString(UiR.string.alarm_not_valid), Toast.LENGTH_SHORT).show()
    }

    private fun initAlarmViews() = with(binding) {
        rlTaskFormAddAlarm.setOnClickListener {
            viewModel.dispatchViewIntent(OnClickAddAlarm)
        }
        alarmDetails.tvAlarmDate.setOnClickListener {
            viewModel.dispatchViewIntent(OnClickAlarmDate)
        }
        alarmDetails.tvAlarmTime.setOnClickListener {
            viewModel.dispatchViewIntent(OnClickAlarmTime)
        }
        alarmDetails.tvAlarmRepeat.setOnClickListener {
            viewModel.dispatchViewIntent(OnClickRepeatAlarm)
        }
    }

    private fun initEditTexts() {
        binding.etTaskFormName.setOnKeyListener(
            View.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    activity?.hideKeyboard()
                    return@OnKeyListener true
                }
                false
            }
        )
    }

    private fun onSelectItemRepeatAlarm(item: SelectableItem) {
        when (item as RepeatAlarmSelectableItem) {
            RepeatAlarmSelectableItem.NOT_REPEAT -> {
                viewModel.dispatchViewIntent(OnSelectAlarmType(RepeatType.NOT_REPEAT))
            }
            RepeatAlarmSelectableItem.DAY -> {
                viewModel.dispatchViewIntent(OnSelectAlarmType(RepeatType.DAY))
            }
            RepeatAlarmSelectableItem.WEEK -> {
                viewModel.dispatchViewIntent(OnSelectAlarmType(RepeatType.WEEK))
            }
            RepeatAlarmSelectableItem.MONTH -> {
                viewModel.dispatchViewIntent(OnSelectAlarmType(RepeatType.MONTH))
            }
            RepeatAlarmSelectableItem.YEAR -> {
                viewModel.dispatchViewIntent(OnSelectAlarmType(RepeatType.YEAR))
            }
            RepeatAlarmSelectableItem.CUSTOM -> {
                viewModel.dispatchViewIntent(OnClickCustomRepeatAlarm)
            }
        }
    }

    private fun updateAlarmVisibility(visible: Boolean, animate: Boolean) {
        if (visible) {
            if (animate) showAlarmWithAnimation() else showAlarm()
        } else {
            if (animate) hideAlarmWithAnimation() else hideAlarm()
        }
    }
}
