package br.com.sailboat.todozy.uicomponent.dialog.weekdays

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.uicomponent.R
import br.com.sailboat.todozy.uicomponent.databinding.VhWeekDayBinding
import br.com.sailboat.todozy.uicomponent.model.DayUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class WeekDayViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<DayUiModel, VhWeekDayBinding>(
        VhWeekDayBinding.inflate(getInflater(parent), parent, false)
    ) {

    init {
        binding.root.setOnClickListener { callback.onClickDay(bindingAdapterPosition) }
    }

    override fun bind(item: DayUiModel) = with(binding) {
        tvWeekDayName.text = item.name

        if (callback.isDaySelected(item.id)) {
            tvWeekDayName.setBackgroundResource(R.drawable.shape_circle_blue)
            tvWeekDayName.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    android.R.color.white
                )
            )
        } else {
            tvWeekDayName.setBackgroundResource(R.drawable.shape_circle)
            tvWeekDayName.setTextColor(
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