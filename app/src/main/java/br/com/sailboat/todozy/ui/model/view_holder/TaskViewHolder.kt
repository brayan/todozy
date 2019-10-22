package br.com.sailboat.todozy.ui.model.view_holder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.isAfterTomorrow
import br.com.sailboat.todozy.domain.helper.isBeforeToday
import br.com.sailboat.todozy.domain.helper.isCurrentYear
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.helper.*
import br.com.sailboat.todozy.ui.model.TaskItemView
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
            alarm?.run {
                updateAlarmText(this)
                updateVisibilityOfAlarmViews(this)
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