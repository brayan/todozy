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
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.task.form.impl.R
import br.com.sailboat.todozy.feature.task.form.impl.databinding.FragmentTaskFormBinding
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnClickAddAlarm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnClickAlarmDate
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnClickAlarmTime
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnClickCustomRepeatAlarm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnClickRepeatAlarm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnClickSaveTask
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnSelectAlarmDate
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnSelectAlarmTime
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnSelectAlarmType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnSelectCustomAlarmType
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewAction.OnStart
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewModel
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.CloseTaskForm
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.HideKeyboard
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.NavigateToAlarmDateSelector
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.NavigateToAlarmTimeSelector
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.NavigateToCustomRepeatAlarmSelector
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.NavigateToRepeatAlarmSelector
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.SetFocusOnInputTaskName
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.SetTaskDetails
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.ShowErrorAlarmNotValid
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.ShowErrorSavingTask
import br.com.sailboat.todozy.feature.task.form.impl.presentation.viewmodel.TaskFormViewState.Action.ShowErrorTaskNameCantBeEmpty
import br.com.sailboat.todozy.utility.android.activity.hideKeyboard
import br.com.sailboat.todozy.utility.android.activity.showKeyboard
import br.com.sailboat.todozy.utility.android.dialog.datetimeselector.DateSelectorDialog
import br.com.sailboat.todozy.utility.android.dialog.datetimeselector.TimeSelectorDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
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

internal class TaskFormFragment : BaseFragment() {

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
            viewModel.dispatchViewAction(onSelectAlarmDateAction)
        }
    }

    private val timeSelectorDialogCallback = object : TimeSelectorDialog.Callback {
        override fun onTimeSelected(hourOfDay: Int, minute: Int) {
            val onSelectAlarmTimeAction = OnSelectAlarmTime(
                hourOfDay = hourOfDay,
                minute = minute,
            )
            viewModel.dispatchViewAction(onSelectAlarmTimeAction)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTaskFormBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        updateCallbacksFromDialogs()

        val taskId = arguments?.getTaskId() ?: Entity.NO_ID
        viewModel.dispatchViewAction(OnStart(taskId))
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

    override fun initViews() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbarTaskForm)
        binding.toolbarTaskForm.setNavigationIcon(R.drawable.ic_close_white_24dp)
        binding.toolbarTaskForm.setNavigationOnClickListener { activity?.onBackPressed() }

        initEditTexts()
        initAlarmViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                val action = OnClickSaveTask(
                    taskName = binding.etTaskFormName.text.toString(),
                    taskNotes = binding.etTaskFormNotes.text.toString(),
                )
                viewModel.dispatchViewAction(action)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
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
        viewModel.viewState.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is CloseTaskForm -> closeTaskForm(action)
                is SetFocusOnInputTaskName -> setFocusOnInputTaskName()
                is SetTaskDetails -> setTaskDetails(action)
                is ShowErrorSavingTask -> showErrorSavingTask()
                is HideKeyboard -> hideKeyboard()
                is ShowErrorAlarmNotValid -> showErrorAlarmNotValid()
                is ShowErrorTaskNameCantBeEmpty -> showErrorTaskNameCantBeBlank()
                is NavigateToAlarmDateSelector -> navigateToAlarmDateSelector(action)
                is NavigateToAlarmTimeSelector -> navigateToAlarmTimeSelector(action)
                is NavigateToRepeatAlarmSelector -> navigateToRepeatAlarmSelector(action)
                is NavigateToCustomRepeatAlarmSelector -> navigateToCustomRepeatAlarmSelector(action)
            }
        }
    }

    private fun closeTaskForm(action: CloseTaskForm) {
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

    private fun setTaskDetails(action: SetTaskDetails) = with(binding) {
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

    private fun navigateToAlarmDateSelector(action: NavigateToAlarmDateSelector) {
        // TODO: EXPERIMENT WITH MATERIAL DATE PICKER
        // https://material.io/components/date-pickers/android
        dateSelectorDialog = DateSelectorDialog.show(
            fragmentManager = childFragmentManager,
            calendar = action.currentDate,
            callback = dateSelectorDialogCallback,
        )
    }

    private fun navigateToAlarmTimeSelector(action: NavigateToAlarmTimeSelector) {
        timeSelectorDialog = TimeSelectorDialog.show(
            fragmentManager = childFragmentManager,
            calendar = action.currentTime,
            callback = timeSelectorDialogCallback,
        )
    }

    private fun navigateToRepeatAlarmSelector(action: NavigateToRepeatAlarmSelector) {
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

    private fun navigateToCustomRepeatAlarmSelector(action: NavigateToCustomRepeatAlarmSelector) {
        WeekDaysSelectorDialog.show(
            childFragmentManager,
            action.days,
            object : WeekDaysSelectorDialog.Callback {
                override fun onClickOk(selectedDays: String) {
                    if (selectedDays.isNotEmpty()) {
                        viewModel.dispatchViewAction(OnSelectCustomAlarmType(selectedDays))
                    } else {
                        viewModel.dispatchViewAction(OnSelectAlarmType(RepeatType.NOT_REPEAT))
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
            viewModel.dispatchViewAction(OnClickAddAlarm)
        }
        alarmDetails.tvAlarmDate.setOnClickListener {
            viewModel.dispatchViewAction(OnClickAlarmDate)
        }
        alarmDetails.tvAlarmTime.setOnClickListener {
            viewModel.dispatchViewAction(OnClickAlarmTime)
        }
        alarmDetails.tvAlarmRepeat.setOnClickListener {
            viewModel.dispatchViewAction(OnClickRepeatAlarm)
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
                viewModel.dispatchViewAction(OnSelectAlarmType(RepeatType.NOT_REPEAT))
            }
            RepeatAlarmSelectableItem.DAY -> {
                viewModel.dispatchViewAction(OnSelectAlarmType(RepeatType.DAY))
            }
            RepeatAlarmSelectableItem.WEEK -> {
                viewModel.dispatchViewAction(OnSelectAlarmType(RepeatType.WEEK))
            }
            RepeatAlarmSelectableItem.MONTH -> {
                viewModel.dispatchViewAction(OnSelectAlarmType(RepeatType.MONTH))
            }
            RepeatAlarmSelectableItem.YEAR -> {
                viewModel.dispatchViewAction(OnSelectAlarmType(RepeatType.YEAR))
            }
            RepeatAlarmSelectableItem.CUSTOM -> {
                viewModel.dispatchViewAction(OnClickCustomRepeatAlarm)
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
