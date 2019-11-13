package br.com.sailboat.todozy.ui.dialog.week_days

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.model.DayView
import kotlinx.android.synthetic.main.vh_week_day.view.*

class WeekDayViewHolder(parent: ViewGroup, private val callback: Callback) :
        BaseViewHolder<DayView>(inflate(parent, R.layout.vh_week_day)) {


    init {
        itemView.setOnClickListener { callback.onClickDay(adapterPosition) }
    }


    override fun bind(item: DayView) = with(itemView) {
        vh_week_day__tv__name.text = item.name

        if (callback.isDaySelected(item.id)) {
            vh_week_day__tv__name.setBackgroundResource(R.drawable.shape_circle_blue)
            vh_week_day__tv__name.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
        } else {
            vh_week_day__tv__name.setBackgroundResource(R.drawable.shape_circle)
            vh_week_day__tv__name.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_blue_grey_500))
        }

    }

    interface Callback {
        fun isDaySelected(id: Int): Boolean
        fun onClickDay(position: Int)
    }
}