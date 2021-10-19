package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.core.extensions.isAfterTomorrow
import br.com.sailboat.todozy.core.extensions.isBeforeToday
import br.com.sailboat.todozy.core.extensions.isCurrentYear
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.TaskItemView
import br.com.sailboat.todozy.databinding.VhTaskBinding
import java.util.*

class TaskViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<TaskItemView, VhTaskBinding>(
        VhTaskBinding.inflate(getInflater(parent), parent, false)
    ) {

    interface Callback {
        fun onClickTask(taskId: Long)
    }

    override fun bind(item: TaskItemView) = with(binding) {
        task.vhTaskTvName.text = item.taskName
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
            e.log()
        }
    }

    private fun updateAlarmText(alarm: Calendar) = with(binding) {

        if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {

            task.vhTaskTvDate.text = if (alarm.isCurrentYear()) {
                alarm.getMonthAndDayShort(context)
            } else {
                alarm.toShortDateView(context)
            }

        } else {
            task.vhTaskTvTime.text = alarm.formatTimeWithAndroidFormat(context)
        }
    }

    private fun updateAlarmColor(alarm: Calendar) = with(binding) {
        task.vhTaskTvDate.setTextColor(AlarmColor().getAlarmColor(context, alarm))
        task.vhTaskTvTime.setTextColor(AlarmColor().getAlarmColor(context, alarm))
    }

    private fun updateVisibilityOfAlarmViews(alarm: Calendar?) = with(binding) {
        if (alarm == null) {
            task.vhTaskTvDate.gone()
            task.vhTaskTvTime.gone()

        } else if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
            task.vhTaskTvTime.gone()
            task.vhTaskTvDate.visible()

        } else {
            task.vhTaskTvTime.visible()
            task.vhTaskTvDate.gone()
        }
    }

}