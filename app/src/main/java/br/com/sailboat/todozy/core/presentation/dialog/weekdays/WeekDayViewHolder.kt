package br.com.sailboat.todozy.core.presentation.dialog.weekdays

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.uicomponent.databinding.VhWeekDayBinding
import br.com.sailboat.todozy.uicomponent.model.DayUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class WeekDayViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<DayUiModel, VhWeekDayBinding>(
        VhWeekDayBinding.inflate(getInflater(parent), parent, false)
    ) {

    init {
        binding.root.setOnClickListener { callback.onClickDay(adapterPosition) }
    }

    override fun bind(item: DayUiModel) = with(binding) {
        vhWeekDayTvName.text = item.name

        if (callback.isDaySelected(item.id)) {
            vhWeekDayTvName.setBackgroundResource(R.drawable.shape_circle_blue)
            vhWeekDayTvName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    android.R.color.white
                )
            )
        } else {
            vhWeekDayTvName.setBackgroundResource(R.drawable.shape_circle)
            vhWeekDayTvName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.md_blue_grey_500
                )
            )
        }

    }

    interface Callback {
        fun isDaySelected(id: Int): Boolean
        fun onClickDay(position: Int)
    }
}