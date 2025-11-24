package br.com.sailboat.uicomponent.impl.viewholder

import android.util.Log
import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getMonthAndDayShort
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeToday
import br.com.sailboat.todozy.utility.kotlin.extension.isCurrentYear
import br.com.sailboat.uicomponent.impl.databinding.VhTaskBinding
import br.com.sailboat.uicomponent.model.TaskUiModel
import java.util.Calendar

class TaskViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<TaskUiModel, VhTaskBinding>(
        VhTaskBinding.inflate(getInflater(parent), parent, false),
    ) {
    interface Callback {
        fun onClickTask(taskId: Long)
    }

    override fun bind(item: TaskUiModel) =
        with(binding) {
            task.tvTaskName.text = item.taskName
            bindTaskAlarm(item)
            root.setOnClickListener { callback.onClickTask(item.taskId) }
        }

    private fun bindTaskAlarm(item: TaskUiModel) {
        try {
            updateVisibilityOfAlarmViews(item.alarm)
            item.alarm?.run {
                updateAlarmText(this)
                updateAlarmColor(item.alarmColor)
            }
        } catch (e: Exception) {
            Log.e("TASK_VIEW_HOLDER", "Error binding alarm", e)
        }
    }

    private fun updateAlarmText(alarm: Calendar) =
        with(binding) {
            if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
                task.tvTaskDate.text =
                    if (alarm.isCurrentYear()) {
                        alarm.getMonthAndDayShort(context)
                    } else {
                        alarm.toShortDateView(context)
                    }
            } else {
                task.tvTaskTime.text = alarm.formatTimeWithAndroidFormat(context)
            }
        }

    private fun updateAlarmColor(alarmColor: Int?) =
        with(binding) {
            alarmColor?.let {
                task.tvTaskDate.setTextColor(alarmColor)
                task.tvTaskTime.setTextColor(alarmColor)
            }
        }

    private fun updateVisibilityOfAlarmViews(alarm: Calendar?) =
        with(binding) {
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
