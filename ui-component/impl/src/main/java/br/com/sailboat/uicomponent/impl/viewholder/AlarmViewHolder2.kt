package br.com.sailboat.uicomponent.impl.viewholder

import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder2
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.uicomponent.impl.databinding.AlarmDetailsBinding
import br.com.sailboat.uicomponent.impl.helper.WeekDaysHelper
import br.com.sailboat.uicomponent.model.AlarmUiModel

class AlarmViewHolder2(val binding: AlarmDetailsBinding) :
    BaseViewHolder2<AlarmDetailsBinding, AlarmUiModel>(binding.root) {

    override fun AlarmDetailsBinding.bind(item: AlarmUiModel) {
        try {
            tvAlarmDate.text = item.date
            tvAlarmTime.text = item.time

            updateAlarmRepeatType(item)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun AlarmDetailsBinding.updateAlarmRepeatType(alarm: AlarmUiModel) {
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
