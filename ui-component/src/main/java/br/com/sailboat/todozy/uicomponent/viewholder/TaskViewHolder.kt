package br.com.sailboat.todozy.uicomponent.viewholder

import android.util.Log
import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.VhTaskBinding
import br.com.sailboat.todozy.uicomponent.model.AlarmColor
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getMonthAndDayShort
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeToday
import br.com.sailboat.todozy.utility.kotlin.extension.isCurrentYear
import java.util.*

class TaskViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<TaskUiModel, VhTaskBinding>(
        VhTaskBinding.inflate(getInflater(parent), parent, false)
    ) {

    interface Callback {
        fun onClickTask(taskId: Long)
    }

    override fun bind(item: TaskUiModel) = with(binding) {
        task.tvTaskName.text = item.taskName
        bindTaskAlarm(item.alarm)
        root.setOnClickListener { callback.onClickTask(item.taskId) }
    }

    private fun bindTaskAlarm(alarm: Calendar?) {
        try {
            updateVisibilityOfAlarmViews(alarm)
            alarm?.run {
                updateAlarmText(this)
                updateAlarmColor(this)
            }
        } catch (e: Exception) {
            Log.e("TASK_VIEW_HOLDER", "Error binding alarm", e)
        }
    }

    private fun updateAlarmText(alarm: Calendar) = with(binding) {

        if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {

            task.tvTaskDate.text = if (alarm.isCurrentYear()) {
                alarm.getMonthAndDayShort(context)
            } else {
                alarm.toShortDateView(context)
            }

        } else {
            task.tvTaskTime.text = alarm.formatTimeWithAndroidFormat(context)
        }
    }

    private fun updateAlarmColor(alarm: Calendar) = with(binding) {
        task.tvTaskDate.setTextColor(AlarmColor().getAlarmColor(context, alarm))
        task.tvTaskTime.setTextColor(AlarmColor().getAlarmColor(context, alarm))
    }

    private fun updateVisibilityOfAlarmViews(alarm: Calendar?) = with(binding) {
        if (alarm == null) {
            task.tvTaskDate.gone()
            task.tvTaskTime.gone()

        } else if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
            task.tvTaskTime.gone()
            task.tvTaskDate.visible()

        } else {
            task.tvTaskTime.visible()
            task.tvTaskDate.gone()
        }
    }

}