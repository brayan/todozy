package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.AlarmUiModel
import br.com.sailboat.todozy.databinding.AlarmDetailsBinding
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible

class AlarmViewHolder(parent: ViewGroup) :
    BaseViewHolder<AlarmUiModel, AlarmDetailsBinding>(
        AlarmDetailsBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: AlarmUiModel): Unit = with(binding) {
        try {
            tvAlarmDate.text = item.dateTime.getFullDateName(itemView.context)
            tvAlarmTime.text = item.dateTime.formatTimeWithAndroidFormat(itemView.context)

            updateAlarmRepeatType(item)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun updateAlarmRepeatType(alarm: AlarmUiModel) = with(binding) {

        if (alarm.repeatType != RepeatTypeUiModel.NOT_REPEAT) {
            tvAlarmRepeat.visible()

            if (alarm.repeatType == RepeatTypeUiModel.CUSTOM) {
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