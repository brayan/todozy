package br.com.sailboat.todozy.core.presentation.dialog.weekdays

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.DayView
import br.com.sailboat.todozy.databinding.VhWeekDayBinding

class WeekDayViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<DayView, VhWeekDayBinding>(
        VhWeekDayBinding.inflate(getInflater(parent), parent, false)
    ) {

    init {
        binding.root.setOnClickListener { callback.onClickDay(adapterPosition) }
    }

    override fun bind(item: DayView) = with(binding) {
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