package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.isAfterTomorrow
import br.com.sailboat.todozy.core.extensions.isBeforeToday
import br.com.sailboat.todozy.core.extensions.isCurrentYear
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.TaskItemView
import kotlinx.android.synthetic.main.task.view.*
import java.util.*

class TaskViewHolder(parent: ViewGroup, callback: Callback) :
        BaseViewHolder<TaskItemView>(inflate(parent, R.layout.vh_task)) {

    init {
        itemView.setOnClickListener { callback.onClickTask(adapterPosition) }
    }

    interface Callback {
        fun onClickTask(position: Int)
    }

    override fun bind(item: TaskItemView) = with(itemView) {
        vh_task__tv__name.text = item.taskName
        bindTaskAlarm(item.alarm)
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

    private fun updateAlarmText(alarm: Calendar) = with(itemView) {

        if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {

            if (alarm.isCurrentYear()) {
                vh_task__tv__date.text = alarm.getMonthAndDayShort(itemView.context)
            } else {
                vh_task__tv__date.text = alarm.toShortDateView(itemView.context)
            }

        } else {
            vh_task__tv__time.text = alarm.formatTimeWithAndroidFormat(itemView.context)
        }
    }

    private fun updateAlarmColor(alarm: Calendar) = with(itemView) {
        vh_task__tv__date.setTextColor(AlarmColor().getAlarmColor(itemView.context, alarm))
        vh_task__tv__time.setTextColor(AlarmColor().getAlarmColor(itemView.context, alarm))
    }

    private fun updateVisibilityOfAlarmViews(alarm: Calendar?) = with(itemView) {
        if (alarm == null) {
            vh_task__tv__date.gone()
            vh_task__tv__time.gone()

        } else if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
            vh_task__tv__time.gone()
            vh_task__tv__date.visible()

        } else {
            vh_task__tv__time.visible()
            vh_task__tv__date.gone()
        }
    }

}