package br.com.sailboat.todozy.feature.task.form.impl.presentation

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.R
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
import br.com.sailboat.todozy.utility.android.dialog.datetimeselector.DateSelectorDialog
import br.com.sailboat.todozy.utility.android.dialog.datetimeselector.TimeSelectorDialog
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.dialog.selectable.SelectItemDialog
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem
import br.com.sailboat.uicomponent.impl.dialog.weekdays.WeekDaysSelectorDialog
import br.com.sailboat.uicomponent.impl.helper.AnimationHelper
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.putTaskId
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TaskFormFragment : Fragment() {

    private val viewModel: TaskFormViewModel by viewModel()

    private lateinit var binding: FragmentTaskFormBinding

    private var dateSelectorDialog: DateSelectorDialog? = null
    private var timeSelectorDialog: TimeSelectorDialog? = null

    private val dateSelectorDialogCallback = object : DateSelectorDialog.Callback {
        override fun onDateSelected(year: Int, month: Int, day: Int) {
            val onSelectAlarmDateAction = OnSelectAlarmDate(
                year = year,
                month = month,
                day = day,
            )
            viewModel.dispatchViewIntent(onSelectAlarmDateAction)
        }
    }

    private val timeSelectorDialogCallback = object : TimeSelectorDialog.Callback {
        override fun onTimeSelected(hourOfDay: Int, minute: Int) {
            val onSelectAlarmTimeAction = OnSelectAlarmTime(
                hourOfDay = hourOfDay,
                minute = minute,
            )
            viewModel.dispatchViewIntent(onSelectAlarmTimeAction)
        }
    }

    companion object {
        fun newInstance() = TaskFormFragment()

        fun newInstance(taskId: Long): TaskFormFragment = with(TaskFormFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        updateCallbacksFromDialogs()

        val taskId = arguments?.getTaskId() ?: Entity.NO_ID
        viewModel.dispatchViewIntent(OnStart(taskId))
    }

    private fun updateCallbacksFromDialogs() {
        dateSelectorDialog = childFragmentManager.findFragmentByTag(DateSelectorDialog.TAG) as? DateSelectorDialog
        dateSelectorDialog?.callback = dateSelectorDialogCallback

        timeSelectorDialog = childFragmentManager.findFragmentByTag(TimeSelectorDialog.TAG) as? TimeSelectorDialog
        timeSelectorDialog?.callback = timeSelectorDialogCallback
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_insert, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                val action = OnClickSaveTask(
                    taskName = binding.etTaskFormName.text.toString(),
                    taskNotes = binding.etTaskFormNotes.text.toString(),
                )
                viewModel.dispatchViewIntent(action)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initViews() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarTaskForm)
        binding.toolbarTaskForm.setNavigationIcon(R.drawable.ic_close_white_24dp)
        binding.toolbarTaskForm.setNavigationOnClickListener { activity?.onBackPressed() }

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
                binding.toolbarTaskForm.setTitle(R.string.edit_task)
            } else {
                binding.toolbarTaskForm.setTitle(R.string.new_task)
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
        Toast.makeText(activity, R.string.msg_error, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    private fun navigateToAlarmDateSelector(action: TaskFormViewAction.NavigateToAlarmDateSelector) {
        // TODO: EXPERIMENT WITH MATERIAL DATE PICKER
        // https://material.io/components/date-pickers/android
        dateSelectorDialog = DateSelectorDialog.show(
            fragmentManager = childFragmentManager,
            calendar = action.currentDate,
            callback = dateSelectorDialogCallback,
        )
    }

    private fun navigateToAlarmTimeSelector(action: TaskFormViewAction.NavigateToAlarmTimeSelector) {
        timeSelectorDialog = TimeSelectorDialog.show(
            fragmentManager = childFragmentManager,
            calendar = action.currentTime,
            callback = timeSelectorDialogCallback,
        )
    }

    private fun navigateToRepeatAlarmSelector(action: TaskFormViewAction.NavigateToRepeatAlarmSelector) {
        SelectItemDialog.show(
            "TAGZERA",
            childFragmentManager,
            getString(R.string.repeat_alarm),
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
        binding.etTaskFormName.error = getString(R.string.exception_task_name)
    }

    private fun showErrorAlarmNotValid() {
        Toast.makeText(activity, getString(R.string.alarm_not_valid), Toast.LENGTH_SHORT).show()
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
