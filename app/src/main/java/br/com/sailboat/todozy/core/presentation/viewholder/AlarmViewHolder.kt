package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.AlarmView
import br.com.sailboat.todozy.databinding.AlarmDetailsBinding
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible

class AlarmViewHolder(parent: ViewGroup) :
    BaseViewHolder<AlarmView, AlarmDetailsBinding>(
        AlarmDetailsBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: AlarmView) = with(binding) {
        try {
            tvAlarmDate.text = item.dateTime.getFullDateName(itemView.context)
            tvAlarmTime.text = item.dateTime.formatTimeWithAndroidFormat(itemView.context)

            updateAlarmRepeatType(item)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun updateAlarmRepeatType(alarm: AlarmView) = with(binding) {

        if (alarm.repeatType != RepeatTypeView.NOT_REPEAT) {
            tvAlarmRepeat.visible()

            if (alarm.repeatType == RepeatTypeView.CUSTOM) {
                tvAlarmRepeat.text =
                    WeekDaysHelper().getCustomRepeat(itemView.context, alarm.customDays!!)
            } else {
                tvAlarmRepeat.setText(alarm.repeatType.description)
            }
        } else {
            tvAlarmRepeat.gone()
        }
    }

}