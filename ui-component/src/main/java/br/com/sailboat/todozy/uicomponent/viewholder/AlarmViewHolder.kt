package br.com.sailboat.todozy.uicomponent.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.AlarmDetailsBinding
import br.com.sailboat.todozy.uicomponent.helper.WeekDaysHelper
import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.todozy.uicomponent.model.AlarmUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible

class AlarmViewHolder(parent: ViewGroup) :
    BaseViewHolder<AlarmUiModel, AlarmDetailsBinding>(
        AlarmDetailsBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: AlarmUiModel): Unit = with(binding) {
        try {
            tvAlarmDate.text = item.date
            tvAlarmTime.text = item.time

            updateAlarmRepeatType(item)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun updateAlarmRepeatType(alarm: AlarmUiModel) = with(binding) {

        if (alarm.shouldRepeat) {
            tvAlarmRepeat.visible()

            if (alarm.isCustom) {
                tvAlarmRepeat.text =
                    WeekDaysHelper().getCustomRepeat(itemView.context, alarm.customDays!!)
            } else {
                tvAlarmRepeat.text = alarm.description
            }
        } else {
            tvAlarmRepeat.gone()
        }
    }

}