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
            tvAlarmDate.text = item.dateTime.getFullDateName(itemView.context)
            tvAlarmTime.text = item.dateTime.formatTimeWithAndroidFormat(itemView.context)

            updateAlarmRepeatType(item)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun updateAlarmRepeatType(alarm: AlarmView) = with(itemView) {

        if (alarm.repeatType != RepeatTypeView.NOT_REPEAT) {
            tvAlarmRepeat.visible()

            if (alarm.repeatType == RepeatTypeView.CUSTOM) {
                tvAlarmRepeat.text = WeekDaysHelper().getCustomRepeat(itemView.context, alarm.customDays!!)
            } else {
                tvAlarmRepeat.setText(alarm.repeatType.description)
            }
        } else {
            tvAlarmRepeat.gone()
        }
    }

}