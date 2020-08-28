package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.AlarmView
import kotlinx.android.synthetic.main.alarm_details.view.*

class AlarmViewHolder(parent: ViewGroup) :
        BaseViewHolder<AlarmView>(inflate(parent, R.layout.alarm_details)) {

    override fun bind(item: AlarmView) = with(itemView) {
        try {
            alarm__tv__date.text = item.dateTime.getFullDateName(itemView.context)
            alarm__tv__time.text = item.dateTime.formatTimeWithAndroidFormat(itemView.context)

            updateAlarmRepeatType(item)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun updateAlarmRepeatType(alarm: AlarmView) = with(itemView) {

        if (alarm.repeatType != RepeatTypeView.NOT_REPEAT) {
            alarm__tv__repeat.visible()

            if (alarm.repeatType == RepeatTypeView.CUSTOM) {
                alarm__tv__repeat.text = WeekDaysHelper().getCustomRepeat(itemView.context, alarm.customDays!!)
            } else {
                alarm__tv__repeat.setText(alarm.repeatType.description)
            }
        } else {
            alarm__tv__repeat.gone()
        }
    }

}